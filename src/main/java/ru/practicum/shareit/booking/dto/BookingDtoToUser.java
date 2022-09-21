package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoToUser {

    private Long id;

    private String start;

    private String end;

    private ItemDto item;

    private UserDto booker;

    private Status status;
}