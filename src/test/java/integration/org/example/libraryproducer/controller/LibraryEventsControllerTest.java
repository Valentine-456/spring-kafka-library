package org.example.libraryproducer.controller;

import org.apache.kafka.shaded.io.opentelemetry.proto.trace.v1.Status;
import org.example.libraryproducer.domain.LibraryEvent;
import org.example.libraryproducer.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryEventsControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void postLibraryEvent() {
        var httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        var httpEntity = new HttpEntity<>(TestUtil.libraryEventRecord(), httpHeaders);

        var responseEntity = restTemplate.exchange(
                "/v1/library-event",
                HttpMethod.POST,
                httpEntity,
                LibraryEvent.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}