package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    /**
     * id вещи в системе, уникальное
     */
    private long id;

    /**
     * название вещи
     */
    private String name;

    /**
     * описание вещи
     */
    private String description;

    /**
     * статус вещи
     * true - можно брать в аренду
     * false - нельзя брать в аренду
     */
    private Boolean available;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;

    private long requestId;
}