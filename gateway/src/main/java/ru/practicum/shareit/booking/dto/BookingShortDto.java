package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidateDateStartAndEnd
public class BookingShortDto {
    long id;
    @ReadOnlyProperty
    long bookerId;

    LocalDateTime start;
    LocalDateTime end;
}
