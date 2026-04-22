package com.salihguneyin.campushub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClubRequest(
        @NotBlank String name,
        @NotBlank String category,
        @NotBlank String leadName,
        @NotBlank @Email String contactEmail,
        @Min(1) Integer memberCount,
        boolean active
) {
}
