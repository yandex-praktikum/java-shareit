package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingResponseDto;
import ru.practicum.shareit.item.comments.model.CommentDto;

import java.util.List;

@Setter
@Getter
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
    private List<CommentDto> comments;
}