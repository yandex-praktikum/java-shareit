package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        return UserMapper.toUserDto(userService.create(user));
    }

    @GetMapping("/{userId}")
    public UserDto findById(@NotNull @Min(1) @PathVariable Long userId) {
        return UserMapper.toUserDto(userService.findById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return UserMapper.toUserDtoList(userService.getAll());
    }

    @PatchMapping("/{userId}")
    public UserDto update(@NotNull @Min(1) @PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        return UserMapper.toUserDto(userService.update(userId, user));
    }

    @DeleteMapping("/{userId}")
    public void delete(@NotNull @Min(1) @PathVariable Long userId) {
        userService.delete(userId);
    }
}