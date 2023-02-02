package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.booking.dto.BookingDto;

import java.util.List;

@Data
@Builder
public class ItemDtoResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
    private Long userId;
}
