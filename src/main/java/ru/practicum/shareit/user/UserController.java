package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Основной контроллер для работы с пользователями
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Возвращает список объектов всех пользователей
     */
    @GetMapping()
    public List<UserDto> findAll() {
        return userService.getUserAll();
    }

    /**
     * Возвращает объект пользователя по ID
     *
     * @param id объекта пользователя
     * @return объект пользователя
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    /**
     * Создаёт объект пользователя
     *
     * @return возвращает объект пользователя, который был создан
     */
    @PostMapping()
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    /**
     * Обновляет данные пользователя
     *
     * @param userId пользователя
     * @return возвращает обновленный объект пользователя
     */
    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId,
                          @RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

    /**
     * Удаляет объект пользователя
     *
     * @param id пользователя, которого удалют
     * @return boolean
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.delete(id);
    }
}
