package ru.practicum.shareit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateTimeStartBeforeEndValidator.class)
public @interface DateTimeStartBeforeEnd {
    String message() default "{\"message\": \"Дата начала не может быть раньше даты окончания\"}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
