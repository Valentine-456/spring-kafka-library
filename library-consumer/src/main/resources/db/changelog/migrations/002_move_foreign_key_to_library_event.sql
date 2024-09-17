ALTER TABLE book
    DROP COLUMN library_event_id;

ALTER TABLE library_event
    ADD COLUMN book_id INTEGER;

ALTER TABLE library_event
    ADD CONSTRAINT fk_library_event_book
        FOREIGN KEY (book_id)
            REFERENCES book(id);