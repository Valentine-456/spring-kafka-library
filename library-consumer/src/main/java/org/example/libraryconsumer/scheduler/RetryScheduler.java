package org.example.libraryconsumer.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.libraryconsumer.entity.FailureRecord;
import org.example.libraryconsumer.entity.FailureRecordStatus;
import org.example.libraryconsumer.repository.FailureRecordRepository;
import org.example.libraryconsumer.service.LibraryEventsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RetryScheduler {
    private final FailureRecordRepository failureRecordRepository;
    private final LibraryEventsService libraryEventsService;

    @Scheduled(fixedRate = 10000)
    public void retryFailedRecords() {
        failureRecordRepository
                .findAllByStatus(FailureRecordStatus.RETRY)
                .forEach(failureRecord -> {
                    log.info("Retry failed record...");
                    var record = buildConsumerRecord(failureRecord);
                    try {
                        libraryEventsService.processLibraryEvent(record);
                        failureRecord.setStatus(FailureRecordStatus.SUCCESS);
                        failureRecordRepository.saveAndFlush(failureRecord);
                    } catch (JsonProcessingException e) {
                        log.error("Error: {}", e.getCause().getMessage());
                    }
                });

    }

    private ConsumerRecord<Integer, String> buildConsumerRecord(FailureRecord failureRecord) {
        return new ConsumerRecord<>(
                failureRecord.getTopic(),
                failureRecord.getPartition(),
                failureRecord.getOffset_value(),
                failureRecord.getKey_value(),
                failureRecord.getErrorRecord()
        );
    }
}
