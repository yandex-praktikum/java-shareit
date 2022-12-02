package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeptions.UserEmailValidationException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) throws UserEmailValidationException {
        UserDto userDtoResponse = userService.addUser(userDto);
        log.info("user has been added");
        return userDtoResponse;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable int userId, @RequestBody UserDto userDto) throws ValidationException, UserEmailValidationException {
        UserDto userDtoResponse = userService.updateUser(userId, userDto);
        log.info("user has been updated");
        return userDtoResponse;
    }

    @GetMapping
    public List<UserDto> findAllUsers() throws ValidationException {
        log.info("amount of users: {}", userService.findAllUsers().size());
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) throws ValidationException {
        UserDto userDto = userService.findUserById(userId);
        log.info("user with id={} has been found", userId);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) throws ValidationException {
        userService.deleteUserById(userId);
        log.info("user with id={} has been deleted", userId);
    }
}
