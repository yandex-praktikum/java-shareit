package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
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
    public UserDto create(@Valid @RequestBody UserDto inputUserDto) throws UserAlreadyExistsException {
        UserDto userDto = userService.create(inputUserDto);
        log.info("Создание пользователя: {}", inputUserDto);
        return userDto;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Получение списка пользователей");
        return userService.getUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        log.info("Пользователь с идентификатором: " + id + " удален.");
        userService.deleteUser(id);
    }
}


