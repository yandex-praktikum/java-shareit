package ru.practicum.shareit.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotBlankConstraintValidator.class)
public @interface NullOrNotBlank {

    String message() default "{value blank or not null}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
