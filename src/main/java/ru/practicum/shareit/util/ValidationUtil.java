package ru.practicum.shareit.util;

import ru.practicum.shareit.util.exception.FoundException;
import ru.practicum.shareit.util.exception.NotFoundException;

public final class ValidationUtil {

    public static <T> T checkNotFoundWithId(T object, int id, String entity) {
        checkNotFoundWithId(object != null, id, entity);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id, String entity) {
        checkNotFound(found, entity + " с id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException(msg + " не найден.");
        }
    }

    public static <T> void checkFound(T object, String msg) {
        checkFound(object != null, msg);
    }

    public static void checkFound(boolean found, String msg) {
        if (found) {
            throw new FoundException(msg + " уже существует.");
        }
    }

}
