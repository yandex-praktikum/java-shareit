package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.sql.Date;

/**
 * Класс бронирования
 */
@Data
@Builder
public class Booking {
    private long id;                //уникальный идентификатор бронирования;
    private Date start;             //дата начала бронирования;
    private Date end;               //дата конца бронирования;
    private Item item;              //вещь, которую пользователь бронирует;
    private User booker;            //пользователь, который осуществляет бронирование;
    private BookingStatus status;   //статус бронирования.
    //Может принимать одно из следующих значений: WAITING, APPROVED, REJECTED, CANCELED
}