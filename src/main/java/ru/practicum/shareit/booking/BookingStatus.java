package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING,  //новая бронь, ожидание подтверждения
    APPROVED,  //бронь подтверждена
    REJECTED,  //бронь отклонена владельцем
    CANCELED,  //бронь отклонена создателем
}
