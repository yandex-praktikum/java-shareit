package ru.practicum.shareit.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankConstraintValidator
        implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return string == null || !string.isBlank();
    }

}
