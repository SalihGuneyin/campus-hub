package com.salihguneyin.campushub.dto;

import com.salihguneyin.campushub.entity.EventFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EventRequest(
        @NotNull Long clubId,
        @NotBlank String title,
        @NotBlank String location,
        @NotNull @FutureOrPresent LocalDate eventDate,
        @NotNull @Min(1) Integer capacity,
        @NotNull EventFormat eventFormat,
        boolean published,
        @NotBlank String summary
) {
}
