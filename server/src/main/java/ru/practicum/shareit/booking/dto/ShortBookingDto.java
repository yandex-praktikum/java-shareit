package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShortBookingDto {

    private int id;
    private int bookerId;

    public ShortBookingDto() {

    }
}
