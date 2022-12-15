package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @NotNull
    Long id;
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime start;
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime end;
    @NotNull
    Item item; //вещь, которую пользователь бронирует
    @NotNull
    User booker; //пользователь, который осуществляет бронирование
    @NotNull
    Status status;
}
