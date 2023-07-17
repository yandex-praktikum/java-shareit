package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

@Data
public class Item {

    private Long id;            //Идентификатор вещи.
    private String name;        //Название вещи.
    private String description; //Описание вещи.
    private Long ownerId;       //ID хозяина вещи.
    private Boolean available;  //Статус для бронирования: свободна, занята.
    @JsonIgnore
    private Boolean isRequest;  //Вещь создана по запросу ищущего пользователя (True - да)?
    private Set<Long> reviews;  //ID фидбеков на вещь.
}
