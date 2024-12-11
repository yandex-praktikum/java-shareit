package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.api.ValidateUpdateRequest;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserSaveDto;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(ValidateCreateRequest.class)
                                             UserSaveDto userSaveDto) {
        return userClient.createUser(userSaveDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Integer userId) {
        return userClient.getUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Integer userId,
                                             @RequestBody @Validated(ValidateUpdateRequest.class)
                                             UserSaveDto userSaveDto) {
        return userClient.updateUser(userId, userSaveDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer userId) {
        userClient.deleteUser(userId);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
