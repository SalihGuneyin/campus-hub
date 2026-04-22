package com.salihguneyin.campushub.service;

import com.salihguneyin.campushub.dto.RegistrationRequest;
import com.salihguneyin.campushub.dto.RegistrationResponse;
import com.salihguneyin.campushub.entity.Event;
import com.salihguneyin.campushub.entity.Registration;
import com.salihguneyin.campushub.entity.RegistrationStatus;
import com.salihguneyin.campushub.repository.EventRepository;
import com.salihguneyin.campushub.repository.RegistrationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            EventService eventService,
            EventRepository eventRepository
    ) {
        this.registrationRepository = registrationRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    public List<RegistrationResponse> getAll() {
        return registrationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RegistrationResponse> getRecent() {
        return registrationRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public RegistrationResponse create(RegistrationRequest request) {
        Event event = eventService.getEvent(request.eventId());

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setAttendeeName(request.attendeeName());
        registration.setAttendeeEmail(request.attendeeEmail());
        registration.setDepartment(request.department());
        registration.setYearOfStudy(request.yearOfStudy());
        registration.setStatus(request.status());
        registration.setNotes(request.notes());

        if (request.status() == RegistrationStatus.APPROVED) {
            int approvedCount = event.getApprovedCount() == null ? 0 : event.getApprovedCount();
            event.setApprovedCount(approvedCount + 1);
            eventRepository.save(event);
        }

        return toResponse(registrationRepository.save(registration));
    }

    private RegistrationResponse toResponse(Registration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getEvent().getTitle(),
                registration.getEvent().getClub().getName(),
                registration.getAttendeeName(),
                registration.getAttendeeEmail(),
                registration.getDepartment(),
                registration.getYearOfStudy(),
                registration.getStatus(),
                registration.getNotes(),
                registration.getCreatedAt()
        );
    }
}
