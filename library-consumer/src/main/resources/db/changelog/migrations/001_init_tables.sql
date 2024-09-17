CREATE SEQUENCE IF NOT EXISTS book_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS library_event_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE book
(
    id               INTEGER NOT NULL,
    book_name        VARCHAR(255),
    book_title       VARCHAR(255),
    library_event_id INTEGER,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

CREATE TABLE library_event
(
    id                 INTEGER NOT NULL,
    library_event_type VARCHAR(255),
    CONSTRAINT pk_library_event PRIMARY KEY (id)
);

ALTER TABLE book
    ADD CONSTRAINT uc_book_library_event UNIQUE (library_event_id);

ALTER TABLE book
    ADD CONSTRAINT FK_BOOK_ON_LIBRARY_EVENT FOREIGN KEY (library_event_id) REFERENCES library_event (id);