package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserServiceImpl userServiceImpl;

    @GetMapping
    public List<UserDto> getAll() {
        return userServiceImpl.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(userServiceImpl.create(UserMapper.toUser(userDto)));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable("userId") Long id) {
        return UserMapper.toUserDto(userServiceImpl.update(UserMapper.toUser(userDto), id));
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable("userId") Long id) {
        return UserMapper.toUserDto(userServiceImpl.getById(id));
    }

    @DeleteMapping("{userId}")
    public void delete(@PathVariable("userId") Long id) {
        userServiceImpl.delete(id);
    }
}
