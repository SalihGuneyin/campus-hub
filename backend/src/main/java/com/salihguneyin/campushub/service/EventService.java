package com.salihguneyin.campushub.service;

import com.salihguneyin.campushub.dto.EventRequest;
import com.salihguneyin.campushub.dto.EventResponse;
import com.salihguneyin.campushub.entity.Club;
import com.salihguneyin.campushub.entity.Event;
import com.salihguneyin.campushub.exception.NotFoundException;
import com.salihguneyin.campushub.repository.ClubRepository;
import com.salihguneyin.campushub.repository.EventRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;

    public EventService(EventRepository eventRepository, ClubRepository clubRepository) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
    }

    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse create(EventRequest request) {
        Club club = clubRepository.findById(request.clubId())
                .orElseThrow(() -> new NotFoundException("Club not found"));

        Event event = new Event();
        event.setClub(club);
        event.setTitle(request.title());
        event.setLocation(request.location());
        event.setEventDate(request.eventDate());
        event.setCapacity(request.capacity());
        event.setApprovedCount(0);
        event.setEventFormat(request.eventFormat());
        event.setPublished(request.published());
        event.setSummary(request.summary());

        return toResponse(eventRepository.save(event));
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    public EventResponse toResponse(Event event) {
        int approvedCount = event.getApprovedCount() == null ? 0 : event.getApprovedCount();
        int seatsLeft = Math.max(event.getCapacity() - approvedCount, 0);

        return new EventResponse(
                event.getId(),
                event.getClub().getId(),
                event.getClub().getName(),
                event.getTitle(),
                event.getLocation(),
                event.getEventDate(),
                event.getCapacity(),
                approvedCount,
                seatsLeft,
                event.getEventFormat(),
                event.isPublished(),
                event.getSummary(),
                event.getCreatedAt()
        );
    }
}
