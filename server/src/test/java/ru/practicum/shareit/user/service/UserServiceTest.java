package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    void addUser_correct() {
        User user = new User(1, "User", "user@mail.ru");
        when(userRepository.save(any())).thenReturn(user);

        service.addUser(user);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addUser_ValidationException() {
        User user = new User(1, "User", "");
        User user2 = new User(2, "User2", "user");
        UserDto user3Dto = new UserDto(3, "", "user3@mail.ru");
        User user3 = UserMapper.toUser(user3Dto);

        Assertions.assertThrows(ValidationException.class, () -> service.addUser(user));
        Assertions.assertThrows(ValidationException.class, () -> service.addUser(user2));
        Assertions.assertThrows(ValidationException.class, () -> service.addUser(user3));
    }

    @Test
    void updateUser_correct() {
        User user = new User(1, "User", "user@mail.ru");
        User update = new User(1, "Update", "update@mail.ru");

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(update);

        service.addUser(user);
        UserDto userDto = service.updateUser(update, 1);

        Assertions.assertEquals("Update", userDto.getName());
    }

    @Test
    void deleteUserTest() {
        service.deleteUser(1);
        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    void getAllUsersTest() {
        User user1 = new User(1, "user1", "user1@mail.ru");
        User user2 = new User(2, "user2", "user2@mail.ru");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        service.getAllUsers();

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_userWasFound() {
        User user = new User(1, "User", "user@mail.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto userDto = service.getUserById(1);

        Assertions.assertEquals("User", userDto.getName());
    }

    @Test
    void getUserById_UserNotFoundException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> service.getUserById(1));
    }
}
