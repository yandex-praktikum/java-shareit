package shareit.user;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Spy
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private UserServiceImpl service;

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final User user2 = User.builder()
            .id(2L)
            .name("user2")
            .email("email2@ya.ru")
            .build();

    private void checkUserDto(UserDto user, UserDto userDto) {
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    public void createUser() {
        when(repository.save(any())).thenReturn(user1);
        UserDto dto = mapper.mapUserDto(user1);

        UserDto result = service.createUser(dto);

        verify(repository, times(1)).save(any());
        checkUserDto(dto, result);
    }

    @Test
    public void getAllUsers() {
        when(repository.findAll()).thenReturn(List.of(user1, user2));
        List<UserDto> result = service.getAllUsers();

        verify(repository, times(1)).findAll();
        assertEquals(result.size(), 2, "некорректное количество пользователей");
    }

    @Test
    public void getUserById() {
        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        UserDto dto = mapper.mapUserDto(user1);

        UserDto result = service.getUserById(user1.getId());

        verify(repository, times(1)).findById(user1.getId());
        checkUserDto(dto, result);
    }

    @Nested
    class Delete {
        @Test
        public void shouldDelete() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.deleteUser(1L));

            assertEquals("Не удалось нйти пользователя с id:1", exception.getMessage());
            verify(repository, never()).deleteById(anyLong());
        }

        @Test
        public void shouldDeleteIfUserIdNotFound() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> service.deleteUser(99L));

            assertEquals("Не удалось нйти пользователя с id:99", exception.getMessage());
            verify(repository, never()).deleteById(anyLong());
        }
    }

}
