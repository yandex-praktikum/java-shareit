package ru.practicum.shareit.booking.dto;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Slf4j
public class DateStartEndValidator implements ConstraintValidator<ValidateDateStartAndEnd, BookingDto> {


    @Override
    public void initialize(ValidateDateStartAndEnd constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BookingDto s, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = s.getStart();
        LocalDateTime end = s.getEnd();

        if (start != null && end != null && start.isBefore(end)) {
            return true;
        }
        return false;
    }
}