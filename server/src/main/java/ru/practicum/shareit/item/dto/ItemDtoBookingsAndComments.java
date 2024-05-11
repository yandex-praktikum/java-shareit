package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDtoBookingsAndComments {

    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private ShortBookingDto lastBooking;

    private ShortBookingDto nextBooking;

    private List<CommentDto> comments;

    public ItemDtoBookingsAndComments() {

    }
}
