package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStorageInMemory implements UserStorage {
    private long id;
    private final List<UserDto> users = new ArrayList<>();

    /**
     * Возвращает список всех пользователей из памяти
     */
    @Override
    public List<UserDto> getUserAll() {
        return users;
    }

    /**
     * Возвращает пользователя по ID из памяти
     */
    @Override
    public UserDto getUserById(long id) {
        return users.stream()
                .filter(p -> p.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден",
                        id)));
    }

    /**
     * Добавляет пользователя в память
     */
    @Override
    public UserDto add(UserDto userDto) {
        checkExistUserInList(userDto);
        userDto.setId(generateId());
        users.add(userDto);

        return userDto;
    }

    /**
     * Обновляет пользователя в памяти
     */
    @Override
    public UserDto update(long id, UserDto userDtoExisting, UserDto userDto) {
        if (!(userDto.getEmail() == null)) {
            checkExistUserInList(userDto);

            userDtoExisting.setEmail(userDto.getEmail());
        }
        if (!(userDto.getName() == null)) {
            userDtoExisting.setName(userDto.getName());
        }
        return userDtoExisting;
    }

    /**
     * Удаляет пользователя из памяти
     */
    @Override
    public void delete(long id) {
        for (UserDto userDtoExisting : users) {
            if (userDtoExisting.getId() == id) {
                users.remove(userDtoExisting);
                return;
            }
        }
    }

    public void checkExistUserInList(UserDto userDto) {
        for (UserDto userDtoExisting : users) {
            if (userDto.getEmail().equals(userDtoExisting.getEmail())) {
                throw new ConflictException(HttpStatus.CONFLICT, String.format("Пользователь с email %s уже существует",
                        userDto.getEmail()));
            }
        }
    }

    long generateId() {
        return ++id;
    }
}