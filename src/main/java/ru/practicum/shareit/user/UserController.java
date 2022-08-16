package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.AlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws AlreadyExistException {
        return userService.add(user);
    }

    @PatchMapping(value = "/{id}")
    public User update(@PathVariable long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable long id) throws NotFoundException {
        return userService.get(id);
    }

    @DeleteMapping(value = "/{id}")
    public void removeUser(@PathVariable long id) {
        userService.remove(id);
    }
}