package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation_label.Create;
import ru.practicum.shareit.validation_label.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    public static final int VALID_ID = 1;
    public static final String USER_ID_ERROR = "ID пользователя не может быть NULL ";

    private final UserMapper mapper;
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Validated({Create.class}) @RequestBody UserDto userDto) {
        User user = mapper.makeModel(userDto, null);
        return mapper.makeDto(userService.createUser(user));
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@NotNull(message = (USER_ID_ERROR))
                                @Min(VALID_ID)
                                @PathVariable Long userId) {
        return mapper.makeDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return mapper.makeUserListToUserDtoList(userService.getAllUsers());
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@NotNull(message = USER_ID_ERROR)
                              @Min(VALID_ID)
                              @PathVariable Long userId,
                              @Validated({Update.class})
                              @RequestBody UserDto userDto) {
        User user = mapper.makeModel(userDto, userId);
        return mapper.makeDto(userService.updateUser(userId, user));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@NotNull(message = (USER_ID_ERROR))
                               @Min(VALID_ID)
                               @PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
