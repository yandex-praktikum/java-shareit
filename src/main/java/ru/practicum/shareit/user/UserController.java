package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ExeptionNotFound;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto inputUserDto) {
        if (inputUserDto.getEmail() == null) {
            throw new ExeptionNotFound("Ошибка! Поле email пустое");
        }
        UserDto userDto = userService.create(inputUserDto);
        log.debug("Создание пользователя: {}", inputUserDto);
        return userDto;
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        List<UserDto> usersDto = userService.findAllUsers();
        log.debug("Получение списка пользователей");
        return usersDto;
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Long userId) {
        UserDto userDto = userService.findUserById(userId);
        log.debug("Получение пользователя по id: {}", userId);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Пользователь с идентификатором: " + userId + " удален.");
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @RequestBody UserDto inputUser, @PathVariable Long userId) {
        UserDto userDto = userService.updateUser(userId, inputUser);
        log.debug("Обновление пользователя: {}", userId);
        return userDto;
    }
}


