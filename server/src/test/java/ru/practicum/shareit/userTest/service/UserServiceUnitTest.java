package ru.practicum.shareit.userTest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldSaveAndReturnUser() {
        User userToSave = new User();
        userToSave.setName("Иван");
        userToSave.setEmail("ivan@mail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Иван");
        savedUser.setEmail("ivan@mail.com");

        when(userRepository.save(userToSave)).thenReturn(savedUser);

        User result = userService.createUser(userToSave);

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Иван"));
        assertThat(result.getEmail(), is("ivan@mail.com"));

        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setName("Иван");
        user.setEmail("ivan@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getName(), is("Иван"));
        assertThat(result.getEmail(), is("ivan@mail.com"));
    }

    @Test
    void getUserById_shouldThrowNotFoundException_whenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Иван");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Петр");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertThat(result, hasSize(2));
    }

    @Test
    void updateUser_shouldUpdateExistingUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Старое имя");
        existingUser.setEmail("old@mail.com");

        User newData = new User();
        newData.setName("Новое имя");
        newData.setEmail("new@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(1L, newData);

        assertThat(result.getName(), is("Новое имя"));
        assertThat(result.getEmail(), is("new@mail.com"));
    }

    @Test
    void deleteUser_shouldDelete_whenUserExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}