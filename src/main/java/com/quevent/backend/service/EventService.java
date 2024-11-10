package com.quevent.backend.service;

import com.quevent.backend.exception.EventNotFoundException;
import com.quevent.backend.exception.UnauthorizedException;
import com.quevent.backend.model.Event;
import com.quevent.backend.model.User;
import com.quevent.backend.repository.EventRepository;
import com.quevent.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Event createEvent(String title, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setUser(user);
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, String title, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event Not Found: " + id));
        event.setTitle(title);
        event.setDescription(description);
        event.setUser(user);
        return eventRepository.save(event);
    }

    public String deleteEvent(Long id, Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event Not Found: " + id));

        if (!event.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You do not have permission to delete this event.");
        }

        eventRepository.deleteById(event.getId());
        return "Event Successfully Deleted";
    }
}
