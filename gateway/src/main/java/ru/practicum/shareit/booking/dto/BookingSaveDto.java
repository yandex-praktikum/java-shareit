package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.DateTimeStartBeforeEnd;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@DateTimeStartBeforeEnd
public class BookingSaveDto {
    @NotNull
    Integer itemId;
    @FutureOrPresent
    LocalDateTime start;
    LocalDateTime end;
}