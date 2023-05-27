package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;
    private final String pathId = "/{id}";

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @GetMapping(pathId)
    public ResponseEntity<Object> getUser(@PathVariable Integer id) {
        return userClient.getUser(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
        return userClient.createUser(user);
    }

    @PatchMapping(pathId)
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody @NotNull UserDto user) {
        return userClient.updateUser(id, user);
    }

    @DeleteMapping(pathId)
    public ResponseEntity<Object> deleteUser(@PathVariable Integer id) {
        return userClient.deleteUser(id);
    }
}
