package com.salihguneyin.campushub.dto;

import com.salihguneyin.campushub.entity.RegistrationStatus;
import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id,
        Long eventId,
        String eventTitle,
        String clubName,
        String attendeeName,
        String attendeeEmail,
        String department,
        Integer yearOfStudy,
        RegistrationStatus status,
        String notes,
        LocalDateTime createdAt
) {
}
