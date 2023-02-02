package ru.practicum.booking;

import org.springframework.stereotype.Component;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;

@Component
public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking fromDto(BookingDto dto) {
        return Booking.builder()
                .id(dto.getId())
                .booker(null)
                .status(Status.WAITING)
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(null)
                .build();
    }

    public static BookingDtoResponse toDtoResponse(Booking booking) { //делаем дто для ответа
            return BookingDtoResponse.builder()
                    .id(booking.getId())
                    .status(booking.getStatus())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .booker(booking.getBooker())
                    .item(booking.getItem())
                    .build();
    }
}
