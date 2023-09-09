package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemShortDto {

    long id;// — уникальный идентификатор вещи;


    String name;// — краткое название;


    String description;//— развёрнутое описание;


    Boolean available;// — статус о том, доступна или нет вещь для аренды;

    long requestId;
}
