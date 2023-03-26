package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BookingDto {
    /**
     * id бронирования в системе, уникальное
     */
    private long id;

    /**
     * вещь, которую бронируют
     */
    private ItemDto item;

    /**
     * пользователь, который бронирует
     */
    private UserDto booker;

    /**
     * начало периода аренды, с даты
     */
    private LocalDateTime start;

    /**
     * окончание периода аренды, по дату (включительно)
     */
    private LocalDateTime end;

    /**
     * подтверждение бронирования хозяином вещи
     */
    private BookingStatus status;
}