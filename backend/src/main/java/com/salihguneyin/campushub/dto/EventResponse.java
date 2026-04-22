package com.salihguneyin.campushub.dto;

import com.salihguneyin.campushub.entity.EventFormat;
import java.time.LocalDate;

public record EventResponse(
        Long id,
        Long clubId,
        String clubName,
        String title,
        String location,
        LocalDate eventDate,
        Integer capacity,
        Integer approvedCount,
        Integer seatsLeft,
        EventFormat eventFormat,
        boolean published,
        String summary,
        LocalDate createdAt
) {
}
