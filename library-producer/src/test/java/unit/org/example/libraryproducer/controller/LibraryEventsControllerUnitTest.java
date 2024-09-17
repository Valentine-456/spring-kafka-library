package org.example.libraryproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.libraryproducer.domain.LibraryEvent;
import org.example.libraryproducer.producer.LibraryEventsProducer;
import org.example.libraryproducer.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryEventsController.class)
class LibraryEventsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    LibraryEventsProducer libraryEventsProducer;

    @Test
    void postLibraryEvent() throws Exception {
        var payload  = objectMapper.writeValueAsString(TestUtil.libraryEventRecord());
        when(libraryEventsProducer.sendLibraryEventWithHeaders(isA(LibraryEvent.class)))
                .thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/library-event")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    void postLibraryEventWithInvalidValues() throws Exception {
        var payload  = objectMapper.writeValueAsString(TestUtil.bookRecordWithInvalidValues());
        when(libraryEventsProducer.sendLibraryEventWithHeaders(isA(LibraryEvent.class)))
                .thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/library-event")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }
}