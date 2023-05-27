package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.IncorrectUserParameterException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    private UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final UserServiceImpl userService = new UserServiceImpl(mockUserRepository);

    @Test
    public void shouldExceptionUserNotFound() {
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.empty());
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUser(1));

        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    public void shouldSuccessUpdateUser() {
        User user = new User(1, "new_alina@email.ru", "Adina");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        UserDto userDto = new UserDto(1, "Alina", "new_alina@email.ru");

        Assertions.assertNotEquals(user.getName(), userDto.getName());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());

        userService.updateUser(1, userDto);

        Assertions.assertEquals(user.getName(), userDto.getName());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void shouldFailUpdateUserWithExistsEmail() {
        User user0 = new User(1, "new_alina@email.ru", "alina");
        User user = new User(2, "new_alina@email.ru", "Adina");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(user0));
        Mockito.when(mockUserRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        UserDto userDto = new UserDto(1, "Alina", "new_alina@email.ru");

        IncorrectUserParameterException exception = Assertions.assertThrows(IncorrectUserParameterException.class, () -> userService.updateUser(1, userDto));

        Assertions.assertNotNull(exception.getParameter());
    }

    @Test
    public void shouldMapToUserDtoList() {
        User user1 = new User(1, "1_alina@email.ru", "Adina");
        User user2 = new User(2, "2_alina@email.ru", "Madina");
        User user3 = new User(3, "3_alina@email.ru", "Alina");

        List<User> userList = List.of(user1, user2, user3);
        List<UserDto> userDtoList = UserMapper.toUserDtoList(userList);

        Assertions.assertEquals(userDtoList.size(), 3);
        Assertions.assertEquals(userDtoList.get(0).getName(), user1.getName());
        Assertions.assertEquals(userDtoList.get(2).getName(), user3.getName());
    }

}
