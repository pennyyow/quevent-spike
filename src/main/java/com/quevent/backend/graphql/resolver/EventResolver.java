package com.quevent.backend.graphql.resolver;

import com.quevent.backend.model.Event;
import com.quevent.backend.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
public class EventResolver {
    private static final Logger logger = LoggerFactory.getLogger(EventResolver.class);
    private final EventRepository eventRepository;

    public EventResolver(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @QueryMapping
    public List<Event> events() {
        return eventRepository.findAll();
    }

    @QueryMapping
    public Event event(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @MutationMapping
    @Transactional
    public Event createEvent(@Argument String title, @Argument String description) {
            logger.debug("Triggered createEvent with title: {} and description: {}", title, description);
            Event event = new Event();
            event.setTitle(title);
            event.setDescription(description);

            // Log event details
            System.out.println("Creating event: " + event);

            return eventRepository.save(event);
    }
}
