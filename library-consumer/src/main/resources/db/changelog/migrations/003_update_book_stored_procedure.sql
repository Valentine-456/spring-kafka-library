CREATE OR REPLACE PROCEDURE update_library_event_and_book(
    p_book_id INTEGER,
    p_book_name VARCHAR(255),
    p_book_title VARCHAR(255),
    p_new_library_event_type VARCHAR(255)
)
    LANGUAGE plpgsql
AS $$
    DECLARE
        v_book_count INTEGER;
        v_new_library_event_id INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_book_count
    FROM book
    WHERE id = p_book_id;

    IF v_book_count > 0 THEN
        UPDATE book
        SET
            book_name = p_book_name,
            book_title = p_book_title
        WHERE id = p_book_id;

        SELECT nextval('library_event_seq') INTO v_new_library_event_id;

        INSERT INTO library_event (id, book_id, library_event_type)
        VALUES (v_new_library_event_id, p_book_id, p_new_library_event_type);
    END IF;
END;
$$;