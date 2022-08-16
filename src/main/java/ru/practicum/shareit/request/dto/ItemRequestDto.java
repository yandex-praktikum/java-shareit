package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

/**
 * Класс отображения для запроса вещи
 */
@Data
@Builder
public class ItemRequestDto {
    private Long id;            //уникальный идентификатор запроса;
    private String description; //текст запроса, содержащий описание требуемой вещи;
    private Long requestor;     //пользователь, создавший запрос;
    private Date created;       //дата и время создания запроса.

}