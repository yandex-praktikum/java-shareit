package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

/**
 * Класс представления для бронирования вещи
 */
@Data
@Builder
public class BookingDto {
    private Long id;        //уникальный идентификатор бронирования;
    private Date start;     //дата начала бронирования;
    private Date end;       //дата конца бронирования;
    private Long item;      //вещь, которую пользователь бронирует;
    private Long booker;    //пользователь, который осуществляет бронирование;
    private String status;  //статус бронирования.
    //Может принимать одно из следующих значений: WAITING, APPROVED, REJECTED, CANCELED
}