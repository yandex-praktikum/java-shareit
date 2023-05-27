package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final String pathId = "/{id}";


    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsersList();
    }

    @GetMapping(pathId)
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @PatchMapping(pathId)
    public UserDto update(@PathVariable Integer id, @RequestBody @NotNull UserDto user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping(pathId)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
