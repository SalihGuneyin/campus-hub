package com.salihguneyin.campushub.dto;

import java.time.LocalDate;

public record ClubResponse(
        Long id,
        String name,
        String category,
        String leadName,
        String contactEmail,
        Integer memberCount,
        boolean active,
        LocalDate createdAt
) {
}
