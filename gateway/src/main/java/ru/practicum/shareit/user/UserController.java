package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * создать пользователя, присвоить id, ручка POST /users
     *
     * @param user провалидированные данные пользователя
     * @return заполненный объект UserDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody User user) {
        log.info("Create user: " + user.toString());

        return UserDtoMapper.toUserDto(userService.create(user));
    }

    /**
     * изменить данные пользователя, ручка PATCH /users/{userId}
     *
     * @param userId  id пользователя
     * @param userDto объект UserDto с данными, которые нужно изменить
     * @return заполненный объект UserDto
     */
    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        userDto.setId(userId);
        log.info("Update user {}: " + userDto, userId);

        User user = UserDtoMapper.toUser(userDto);

        return UserDtoMapper.toUserDto(userService.update(user));
    }

    /**
     * запрос данных всех пользователей (GET /users)
     *
     * @return список объектов User
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        log.info("Get all users");
        return UserDtoMapper.toUserDtoList(userService.getAll());
    }

    /**
     * запрос данных пользователя  (GET /users/{userId})
     *
     * @param userId id пользователя
     * @return список объектов User
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto get(@PathVariable Long userId) {
        log.info("Get userId {}", userId);
        return UserDtoMapper.toUserDto(userService.getById(userId));
    }

    /**
     * удалить пользователя (DELETE /users/{userId})
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int userId) {
        log.info("Delete userId {}", userId);
        userService.delete(userId);
    }
}