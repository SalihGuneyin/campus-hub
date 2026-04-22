package com.salihguneyin.campushub.dto;

import com.salihguneyin.campushub.entity.RegistrationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull Long eventId,
        @NotBlank String attendeeName,
        @NotBlank @Email String attendeeEmail,
        @NotBlank String department,
        @NotNull @Min(1) @Max(8) Integer yearOfStudy,
        @NotNull RegistrationStatus status,
        @NotBlank String notes
) {
}
