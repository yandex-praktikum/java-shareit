package ru.practicum.shareit.request.model;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequest {
    Long id;                //уникальный идентификатор запроса;
    String description;     //текст запроса, содержащий описание требуемой вещи;
    Long requester;         //пользователь, создавший запрос;
    LocalDateTime created;  //дата и время создания запроса.
}
