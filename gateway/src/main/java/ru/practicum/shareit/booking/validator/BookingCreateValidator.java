package ru.practicum.shareit.booking.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

/**
 * Класс для дополнительной валидации данных бронирования
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingCreateValidator {
    public static void validate(BookingCreateRequest bookingCreateRequest) throws ValidationException {
        //дата начала должна быть ранее даты конца
        if (bookingCreateRequest.getEnd().isBefore(bookingCreateRequest.getStart())) {
            throw new ValidationException("Дата начала бронирования должна быть раньше даты окончания");
        }

        //дата начала должна быть в будущем
        if (bookingCreateRequest.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата начала бронирования должна быть в будущем");
        }
    }
}