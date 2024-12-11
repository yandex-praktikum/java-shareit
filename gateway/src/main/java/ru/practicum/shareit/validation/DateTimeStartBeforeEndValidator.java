package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingSaveDto;

import java.time.LocalDateTime;

public class DateTimeStartBeforeEndValidator implements ConstraintValidator<DateTimeStartBeforeEnd, BookingSaveDto> {

    @Override
    public void initialize(DateTimeStartBeforeEnd constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingSaveDto bookingSaveDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingSaveDto.getStart();
        LocalDateTime end = bookingSaveDto.getEnd();
        return start != null && end != null && !start.isAfter(end);
    }
}
