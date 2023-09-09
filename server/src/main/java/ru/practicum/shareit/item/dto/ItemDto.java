package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.ItemUpdateMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    long id;// — уникальный идентификатор вещи;


    String name;// — краткое название;


    String description;//— развёрнутое описание;


    Boolean available;// — статус о том, доступна или нет вещь для аренды;

    long owner;// — владелец вещи;
    Long requestId;// — если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос.


    BookingShortDto lastBooking;


    BookingShortDto nextBooking;


    List<CommentDto> comments;

}
