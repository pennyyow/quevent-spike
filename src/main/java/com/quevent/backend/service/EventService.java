package com.quevent.backend.service;

import com.quevent.backend.exception.EventNotFoundException;
import com.quevent.backend.model.Event;
import com.quevent.backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event updateEvent(Long id, String title, String description) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event Not Found: " + id));
        event.setTitle(title);
        event.setDescription(description);
        return eventRepository.save(event);
    }

    public String deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event Not Found: " + id));
        eventRepository.deleteById(event.getId());
        return "Event Successfully Deleted";
    }
}
