package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exceptions.UserEmailNotUniqueException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserServiceTest {

    @Test
    void getByIdUserNotFound() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.getById(1));

        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    void getByIdUserFound() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        User user = userService.getById(1);

        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("name", user.getName());
        Assertions.assertEquals("e@mail.ru", user.getEmail());
    }

    @Test
    void createUserNormalWay() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(new User(1, "name", "e@mail.ru"));

        User user = userService.create(new User(1, "name", "e@mail.ru"));

        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("name", user.getName());
        Assertions.assertEquals("e@mail.ru", user.getEmail());
    }

    @Test
    void createUserDuplicatedEmail() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        final UserEmailNotUniqueException exception = Assertions.assertThrows(
                UserEmailNotUniqueException.class,
                () -> userService.create(new User(1, "name", "e@mail.ru")));

        Assertions.assertEquals("E-mail не уникален", exception.getMessage());
    }

    @Test
    void updateUserDuplicatedEmail() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        final UserEmailNotUniqueException exception = Assertions.assertThrows(
                UserEmailNotUniqueException.class,
                () -> userService.update(new User(1, "name", "e@mail.ru")));

        Assertions.assertEquals("E-mail не уникален", exception.getMessage());
    }

    @Test
    void updateUserIfInputNull() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);
        assertNull(userService.update(null));
    }

    @Test
    void updateUserIfInputUserIdZero() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);
        assertNull(userService.update(new User(0, "name", "e@mail.ru")));
    }

    @Test
    void updateUserNormalWay() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(new User(1, "name", "e@mail.ru"));

        User user = userService.update(new User(1, "name", "e@mail.ru"));

        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("name", user.getName());
        Assertions.assertEquals("e@mail.ru", user.getEmail());
    }

    @Test
    void updateIfUserNotExists() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.update(new User(1, "name", "e@mail.ru")));

        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    void getAllTest() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(List.of(new User(1, "name", "e@mail.ru")));

        List<User> userList = userService.getAll();

        assertEquals(1, userList.size());
        assertEquals(1, userList.get(0).getId());
    }

    @Test
    void deleteTest() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        User userToDelete = new User(1, "name", "e@mail.ru");
        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(userToDelete));

        userService.delete(1);

        Mockito.verify(mockUserRepository, Mockito.times(1))
                .delete(userToDelete);
    }
}