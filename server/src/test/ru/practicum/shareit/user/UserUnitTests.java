package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserServiceImpl;
import ru.practicum.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserUnitTests {

    private UserServiceImpl userService;
    private User user;
    private UserDto userDto;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        user = new User(1L, "User1", "user1@mail.com");
        userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    @Test
    void getUserByIdTest() {
        assertThrows(UserNotFoundException.class, () -> userService.getById(1L));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        assertThat(userService.getById(1L).getId().equals(1L));
    }

    @Test
    void findAllUsersTest() {
        Mockito
                .when(userRepository.findAllUsers())
                .thenReturn(List.of(user));

        assertThat(userService.getAll().size() == 1);
    }

    @Test
    void creteUserTestException() {
        Mockito
                .when(userRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateException.class, () -> userService.create(userDto));

        userDto.setEmail(null);

        assertThrows(ValidationException.class, () -> userService.create(userDto));
    }

    @Test
    void createUserTest() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);
        assertThat(userService.create(userDto).getId().equals(1L));
    }

    @Test
    void updateUserTest() {
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);

        assertThat(userService.put(userDto, 1L).getName().equals("User1"));
    }


    @Test
    void deleteTest() {
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        assertThat(userService.delete(1L));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.delete(1L));

    }
}