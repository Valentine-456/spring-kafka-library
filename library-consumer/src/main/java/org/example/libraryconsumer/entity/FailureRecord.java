package org.example.libraryconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FailureRecord {
    @Id
    @GeneratedValue
    private Integer id;

    private String topic;

    private Integer key_value;

    private String errorRecord;

    private Integer partition;

    private Long offset_value;

    private String exception;

    @Enumerated(EnumType.STRING)
    private FailureRecordStatus status;
}
