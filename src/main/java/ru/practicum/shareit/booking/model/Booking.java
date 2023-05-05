package ru.practicum.shareit.booking.model;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@Builder
public class Booking {
    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Item item;
    @NotNull
    private User booker;
    private Status status;
}

