package com.quevent.backend.controller;

import com.quevent.backend.model.Event;
import com.quevent.backend.repository.EventRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final EventRepository eventRepository;

    public TestController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/test-connection")
    public List<Event> testConnection() {
        try {
            return eventRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }
}