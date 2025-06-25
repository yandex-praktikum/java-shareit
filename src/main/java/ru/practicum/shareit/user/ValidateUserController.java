package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Component
public class ValidateUserController {

    private final UserRepository userRepository;

    public ValidateUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserDto(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            log.warn("name не может быть пустым\"");
            throw new NotDataException("name не может быть пустым");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.warn("такой email уже есть");
            throw new DuplicateDataException("такой email уже есть");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            log.warn("email не может быть пустым");
            throw new NotDataException("email не может быть пустым");
        }

        if (!userDto.getEmail().contains("@")) {
            log.warn("введен не email");
            throw new NotDataException("введен не email");
        }
    }
}
