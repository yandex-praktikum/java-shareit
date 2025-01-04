package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserEmailIsNotUnique;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private RequestUserDto requestUserDto;

    @BeforeEach
    public void setUp() {
        requestUserDto = new RequestUserDto("User-1", "user@gmail.com");
    }

    @Test
    public void create_userShouldBeCreated() {
        UserDto savedUser = userService.create(requestUserDto);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(requestUserDto.getName());
        assertThat(savedUser.getEmail()).isEqualTo(requestUserDto.getEmail());
    }

    @Test
    public void create_userShouldBeThrowUserEmailIsNotUnique() {
        userRepository.save(new User(null, "User-2", requestUserDto.getEmail()));

        assertThatThrownBy(() -> userService.create(requestUserDto))
                .isInstanceOf(UserEmailIsNotUnique.class)
                .hasMessage("Пользователь с таким email уже существует");
    }

    @Test
    public void update_userShouldBeUpdated() {
        User storageUser = userRepository.save(new User(null, "User-2", "user2@gmail.com"));
        userService.update(storageUser.getId(), requestUserDto);

        User updated = userRepository.findById(storageUser.getId()).get();
        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(storageUser.getId());
        assertThat(updated.getName()).isEqualTo(storageUser.getName());
        assertThat(updated.getEmail()).isEqualTo(storageUser.getEmail());
    }

    @Test
    public void update_shouldThrowUserEmailIsNotUnique() {
        User storageUser1 = userRepository.save(new User(null, "User-1", "user1@gmail.com"));
        User storageUser2 = userRepository.save(new User(null, "User-2", "user2@gmail.com"));

        requestUserDto.setEmail(storageUser1.getEmail());
        assertThatThrownBy(() -> userService.update(storageUser2.getId(), requestUserDto))
                .isInstanceOf(UserEmailIsNotUnique.class);
    }

    @Test
    public void update_shouldThrowNotFoundException() {
        assertThatThrownBy(() -> userService.update(999L, requestUserDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void findUserById_shouldReturnUser() {
        User user = new User(null, "User-1", "user1@gmail.com");
        Long userId = userRepository.save(user).getId();
        UserDto foundUser = userService.findUserById(userId);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(userId);
        assertThat(foundUser.getName()).isEqualTo(user.getName());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void findUserById_shouldThrowNotFoundException() {
        assertThatThrownBy(() -> userService.findUserById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void checkExistsById_shouldBeSuccessfully() {
        Long userId = userRepository.save(new User(null, "User-1", "user1@gmail.com")).getId();
        assertDoesNotThrow(() -> userService.checkExistsById(userId));
    }

    @Test
    public void checkExistsById_shouldThrowNotFoundException() {
        Long userId = 999L;
        assertThatThrownBy(() -> userService.checkExistsById(userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void existsById_shouldReturnTrue() {
        Long userId = userRepository.save(new User(null, "User-1", "user1@gmail.com")).getId();
        assertThat(userService.existsById(userId)).isTrue();
    }

    @Test
    public void existsById_shouldReturnFalse() {
        assertThat(userService.existsById(999L)).isFalse();
    }

    @Test
    public void findByEmail_shouldReturnPresentOpt() {
        User user = new User(null, "User-1", "user1@gmail.com");
        User savedUser = userRepository.save(user);
        String userEmail = user.getEmail();

        Optional<UserDto> foundUser = userService.findByEmail(userEmail);
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getName()).isEqualTo(user.getName());
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void findByEmail_shouldReturnEmptyOpt() {
        String userEmail = "user@example.com";
        Optional<UserDto> foundUser = userService.findByEmail(userEmail);
        assertThat(foundUser.isEmpty()).isTrue();
    }

    @Test
    public void getAll_shouldReturnUsers() {
        User user = new User(null, "User-1", "user1@gmail.com");
        userRepository.save(user);

        Collection<UserDto> users = userService.getAll();
        assertThat(users).isNotNull();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void getAll_shouldReturnEmptyCollectionOfUsers() {
        Collection<UserDto> users = userService.getAll();
        assertThat(users).isNotNull();
        assertThat(users).isEmpty();
    }
}
