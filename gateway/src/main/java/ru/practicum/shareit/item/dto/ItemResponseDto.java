package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Setter
@Getter
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
    private List<CommentDto> comments;
}