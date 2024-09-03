package ru.practicum.shareit.booking.model;

public enum Status {

    WAITING, //бронирование в статусе "ожижания"
    APPROVED, //бронирование подтверждено
    REJECTED, //бронирование отменено владельцем
    CANCELED //бронирование отменено создателем
}