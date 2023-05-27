package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;

    private BookingDto nextBooking;
    private BookingDto lastBooking;
    private List<CommentDto> comments;


}
