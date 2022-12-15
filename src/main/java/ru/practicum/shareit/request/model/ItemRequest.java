package ru.practicum.shareit.request.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    @NotNull
    Long id;
    @NotNull
    String description;
    @NotNull
    User requester; //пользователь, создавший запрос
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime created; //дата и время создания запроса
}
