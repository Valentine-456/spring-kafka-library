package org.example.libraryconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.example.libraryconsumer.entity.FailureRecordStatus;
import org.example.libraryconsumer.service.FailureService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventsConsumerConfig {
    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    FailureService failureService;

    @Value("${topics.dead-letter}")
    private String deadLetterTopic;

    @Value("${topics.retry}")
    private String retryTopic;

    private DeadLetterPublishingRecoverer provideDeadLetterRecoverer() {
        var recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (r, e) -> {
                    if(e.getCause() instanceof RecoverableDataAccessException) {
                        return new TopicPartition(retryTopic, r.partition());
                    } else {
                        return new TopicPartition(deadLetterTopic, r.partition());
                    }
                });
        return recoverer;
    }

    ConsumerRecordRecoverer consumerRecordRecoverer = (consumerRecord, e) -> {
        var record = (ConsumerRecord<Integer, String>) consumerRecord;

        if(e.getCause() instanceof RecoverableDataAccessException) {
            failureService.saveFailedRecord(record, e, FailureRecordStatus.RETRY);
        } else {
            failureService.saveFailedRecord(record, e, FailureRecordStatus.DEAD_LETTER);
        }
    };

    private DefaultErrorHandler provideErrorHandler() {
//        FixedBackOff backOff = new FixedBackOff(1000L, 4);
        ExponentialBackOff backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(3.0);

        var defaultErrorHandler = new DefaultErrorHandler(
//                provideDeadLetterRecoverer(),
                consumerRecordRecoverer,
                backOff
        );
        var notRetryableErrors = List.of(IllegalArgumentException.class);
        notRetryableErrors.forEach(defaultErrorHandler::addNotRetryableExceptions);
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn(
                    "Failed record delivery attempt \nException: {}\ndeliveryAttempt: {}",
                    ex,
                    deliveryAttempt
            );
        });

        return defaultErrorHandler;
    }

    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();


        configurer.configure(factory, kafkaConsumerFactory);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConcurrency(3);
        factory.setCommonErrorHandler(provideErrorHandler());
        return factory;
    }
}
