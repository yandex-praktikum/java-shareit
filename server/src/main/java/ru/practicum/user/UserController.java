package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    public UserDto get(@Valid @PathVariable("userId") Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto user) {
        return service.create(user);
    }

    @PatchMapping("/{userId}")
    public User put(@Valid @RequestBody UserDto user, @PathVariable("userId") Long id) {
        return service.put(user, id);
    }

    @DeleteMapping("/{userId}")
    public Boolean delete(@PathVariable("userId") Long userId) {
        return service.delete(userId);
    }
}
