package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingStatusDto {
    Long id;
    @Future
    LocalDateTime start;
    @Future
    LocalDateTime end;
    Status status;
    UserDto booker;
    ItemInputDto item;
}
