package ru.practicum.shareit.user.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EmailIsPresentValidator implements ConstraintValidator<EmailIsPresent, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (userService.findAll().stream().collect(Collectors.toList())
                       .stream().filter((user -> user.getEmail().equals(value))).count() == 0) {
            return true;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is present.");
        }

    }

}