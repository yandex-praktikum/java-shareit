package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User add(@Valid @RequestBody User userToAdd) {
        return userService.add(userToAdd);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody UserDto userToUpdate, @PathVariable Long userId) {
        return userService.update(userToUpdate, userId);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getOne(@PathVariable Long userId) {
        return userService.getOne(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
