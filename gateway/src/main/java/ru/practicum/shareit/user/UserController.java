package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @Validated({UserCreateGroupMarker.class})
            @RequestBody UserDto userDto) {
        log.info("Начало сохранение пользователя {}", userDto);
        return userClient.create(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Начало запроса всех пользователей");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable
                                      @NotNull
                                      Long id) {
        log.info("Начало запроса  пользователя с id={}", id);
        return userClient.get(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable
                                         @NotNull
                                         Long id) {
        log.info("Начало удаления  пользователя с id={}", id);
       return userClient.delete(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(
            @Validated(UpdateGroupMarker.class)
            @RequestBody
            UserDto userDto,
            @PathVariable
            @NotNull
            Long id) {
        log.info("Начало обновления  пользователя {} с id={}", userDto, id);
        return userClient.update(id, userDto);
    }
}
