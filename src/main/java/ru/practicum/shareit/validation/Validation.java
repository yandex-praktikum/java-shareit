package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeptions.UserEmailValidationException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Component
public class Validation {

    public static void validateUserId(UserStorage userStorage, Integer userId) throws ValidationException {
        if (userId != null && userId <= 0) {
            log.info("userId can't be negative");
            throw new ValidationException("userId can't be negative");
        }
        if (userStorage.findUserById(userId) == null) {
            log.info("user with id " + userId + " not found");
            throw new ValidationException("user with id " + userId + " not found");
        }
    }

    public static void validateUserIdForItem(ItemStorage itemStorage, Integer userId, Integer itemId) throws ValidationException {
        if (itemStorage.findItemById(itemId).getUserId() != userId) {
            log.info("user with id " + userId + " can't update item with id " + itemId);
            throw new ValidationException("user with id " + userId + " can't update item with id " + itemId);
        }
    }

    public static void validateUserEmail(UserStorage userStorage, String email) throws UserEmailValidationException {
        if (userStorage.findAllUsers().stream().map(UserDto::getEmail).anyMatch(x -> x.equals(email))) {
            log.info("user with email " + email + " already exist");
            throw new UserEmailValidationException("user with email " + email + " already exist");
        }
    }

    public static void validateItemId(ItemStorage itemStorage, Integer itemId) throws ValidationException {
        if (itemId != null && itemId <= 0) {
            log.info("itemId can't be negative");
            throw new ValidationException("itemId can't be negative");
        }
        if (itemStorage.findItemById(itemId) == null) {
            log.info("item with id " + itemId + " not found");
            throw new ValidationException("item with id " + itemId + " not found");
        }
    }
}
