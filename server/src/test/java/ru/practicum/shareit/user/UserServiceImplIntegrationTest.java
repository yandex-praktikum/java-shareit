package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User(null, "John Doe", "john@mail.com");
        existingUser = userRepository.save(existingUser);
    }

    @Test
    void createUser_whenValidData_thenUserCreated() {
        UserDto newUserDto = new UserDto(null, "Jane Doe", "jane@mail.com");

        UserDto result = userService.createUser(newUserDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane@mail.com", result.getEmail());

        // Проверяем в БД
        User savedUser = userRepository.findById(result.getId()).orElseThrow();
        assertEquals("Jane Doe", savedUser.getName());
        assertEquals("jane@mail.com", savedUser.getEmail());
    }

    @Test
    void createUser_whenDuplicateEmail_thenThrowException() {
        UserDto duplicateEmailUser = new UserDto(null, "Another John", "john@mail.com");

        assertThrows(DuplicateEmailException.class,
                () -> userService.createUser(duplicateEmailUser));
    }

    @Test
    void updateUser_whenUpdateName_thenOnlyNameUpdated() {
        UserDto updateDto = new UserDto(null, "John Updated", null);

        UserDto result = userService.updateUser(existingUser.getId(), updateDto);

        assertEquals("John Updated", result.getName());
        assertEquals("john@mail.com", result.getEmail());

        User updatedUser = userRepository.findById(existingUser.getId()).orElseThrow();
        assertEquals("John Updated", updatedUser.getName());
        assertEquals("john@mail.com", updatedUser.getEmail());
    }

    @Test
    void updateUser_whenUpdateEmail_thenEmailChanged() {
        UserDto updateDto = new UserDto(null, null, "john.new@mail.com");

        UserDto result = userService.updateUser(existingUser.getId(), updateDto);

        assertEquals("John Doe", result.getName());
        assertEquals("john.new@mail.com", result.getEmail());

        User updatedUser = userRepository.findById(existingUser.getId()).orElseThrow();
        assertEquals("john.new@mail.com", updatedUser.getEmail());
    }

    @Test
    void getAllUsers_whenUsersExist_thenReturnAllUsers() {
        User secondUser = new User(null, "Second User", "second@mail.com");
        userRepository.save(secondUser);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("john@mail.com")));
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals("second@mail.com")));
    }

    @Test
    void deleteUser_whenUserExists_thenUserDeleted() {
        userService.deleteUser(existingUser.getId());

        assertFalse(userRepository.existsById(existingUser.getId()));
    }

    @Test
    void deleteUser_whenUserNotExists_thenThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUser(999L));
    }
}
