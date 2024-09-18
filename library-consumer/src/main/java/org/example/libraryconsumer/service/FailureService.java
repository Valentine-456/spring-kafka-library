package org.example.libraryconsumer.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.libraryconsumer.entity.FailureRecord;
import org.example.libraryconsumer.entity.FailureRecordStatus;
import org.example.libraryconsumer.repository.FailureRecordRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class FailureService {
    private FailureRecordRepository failureRecordRepository;


    public void saveFailedRecord(ConsumerRecord<Integer, String> consumerRecord, Exception e, FailureRecordStatus failureRecordStatus) {
        var failureRecord = FailureRecord.builder()
                .id(null)
                .key_value(consumerRecord.key())
                .errorRecord(consumerRecord.value())
                .topic(consumerRecord.topic())
                .partition(consumerRecord.partition())
                .offset_value(consumerRecord.offset())
                .exception(e.getCause().getMessage())
                .status(failureRecordStatus)
                .build();

        failureRecordRepository.save(failureRecord);
    }
}
