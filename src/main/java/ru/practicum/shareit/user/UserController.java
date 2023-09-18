package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Начало обработки запроса на получение всех пользователей");
        List<UserDto> usersDto = userService.getUsers();
        log.info("Завершение обработки запроса на получение всех пользователей");
        return usersDto;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable @Positive Long userId) {
        log.info("Начало обработки запроса на получение пользователя с id={}", userId);
        UserDto userDto = userService.getUser(userId);
        log.info("Завершение обработки запроса на получение пользователя с id={}", userId);
        return userDto;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Начало обработки запроса на создание пользователя: {}", userDto);
        UserDto createdUserDto = userService.createUser(userDto);
        log.info("Завершение обработки запроса на создание пользователя: {}", userDto);
        return createdUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto updatedUserDto, @PathVariable @Positive Long userId) {
        updatedUserDto.setId(userId);
        log.info("Начало обработки запроса на обновление пользователя: {}", updatedUserDto);
        UserDto userDto = userService.updateUser(updatedUserDto);
        log.info("Завершение обарботки запроса на обновление пользователя: {}", userDto);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.info("Начало обработки запроса на удаление пользователя с id={}", userId);
        userService.deleteUser(userId);
        log.info("Завершение обработки запроса на удаление пользователя с id={}", userId);
    }
}
