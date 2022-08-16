package ru.practicum.shareit.item;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * Класс вещи для аренды
 */
@Data
@AllArgsConstructor
public class Item {
    @NotNull
    private long id;            //уникальный идентификатор вещи;
    private String name;        //краткое название;
    private String description; //развёрнутое описание;
    private Boolean available;  //статус о том, доступна или нет вещь для аренды;
    private long owner;         //владелец вещи;
    private ItemRequest request; /*если вещь была создана по запросу другого пользователя,
     то в этом поле будет храниться
    ссылка на соответствующий запрос.*/
}