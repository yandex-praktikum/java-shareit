package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable long userId) {
        return userService.get(userId);
    }

    @PostMapping
    public UserDTO create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDTO update(@PathVariable long userId, @RequestBody User user) {
        return userService.update(user, userId);
    }

    @DeleteMapping("/{userId}")
    public UserDTO delete(@PathVariable long userId) {
        return userService.delete(userId);
    }
}