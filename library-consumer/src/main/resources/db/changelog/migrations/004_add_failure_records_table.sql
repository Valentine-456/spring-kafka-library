CREATE SEQUENCE IF NOT EXISTS failure_record_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE failure_record
(
    id           INTEGER NOT NULL,
    topic        VARCHAR(255),
    key_value    INTEGER,
    error_record VARCHAR(255),
    partition    INTEGER,
    offset_value BIGINT,
    exception    VARCHAR(255),
    status       VARCHAR(255),
    CONSTRAINT pk_failurerecord PRIMARY KEY (id)
);