package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoState {
    private Long id;

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    @NotEmpty
    @NotNull
    private ItemDto item;

    @NotEmpty
    @NotNull
    private UserDto booker;

    private Status status;

    private List<State> states;
}
