package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    long id;

    @NotEmpty
    String name;

    @NotEmpty
    String description;

    @NotNull
    Boolean available;

    Long requestId;

    BookingDtoItem nextBooking;

    BookingDtoItem lastBooking;

    List<CommentDto> comments;
}
