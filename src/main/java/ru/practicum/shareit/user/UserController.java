package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService service;


    /**
     * Добавить юзера в БД.
     * @param userDto пользователь.
     * @return добавляемый пользователь.
     */
    @PostMapping
    ResponseEntity<UserDto> addToStorage(@RequestBody @Validated(CreateObject.class) UserDto userDto) {
        UserDto createdUser = service.addToStorage(userDto);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Обновить юзера в БД.
     * @param userDto пользователь
     * @param userId  ID обновляемого пользователя.
     * @return обновлённый пользователь.
     */
    @PatchMapping("/{userId}")
    UserDto updateInStorage(@PathVariable long userId,
                            @Validated({UpdateObject.class}) @RequestBody UserDto userDto) {
        return service.updateInStorage(userDto, userId);
    }

    /**
     * Удалить пользователя из БД.
     * @param userId ID удаляемого пользователя.
     */
    @DeleteMapping("/{userId}")
    ResponseEntity<String> removeFromStorage(@PathVariable Long userId) {
        String message = service.removeFromStorage(userId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Получить список всех пользователей.
     * @return список пользователей.
     */
    @GetMapping
    ResponseEntity<List<UserDto>> getAllUsersFromStorage() {
        List<UserDto> allUsersDto = service.getAllUsers();
        return new ResponseEntity<>(allUsersDto, HttpStatus.OK);
    }

    /**
     * Получить пользователя по ID.
     * @param userId ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(service.getUserById(userId), HttpStatus.OK);
    }
}
