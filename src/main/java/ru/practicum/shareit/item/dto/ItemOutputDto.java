package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingItemOutputDto;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemOutputDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingItemOutputDto lastBooking;
    BookingItemOutputDto nextBooking;
    List<CommentDto> comments;
}
