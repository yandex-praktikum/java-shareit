package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ItemDto {
    Long id;            //Идентификатор вещи.
    String name;        //Название вещи.
    String description; //Описание вещи.
    Long ownerId;       //ID хозяина вещи.
    Boolean available;  //Статус для бронирования: свободна, занята.
    Boolean isRequest;  //Вещь создана по запросу ищущего пользователя (True - да)?
    Set<Long> reviews;  //ID фидбеков на вещь.
}
