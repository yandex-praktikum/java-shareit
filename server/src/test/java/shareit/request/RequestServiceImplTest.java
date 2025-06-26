package shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.request.dto.FullRequestDto;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    private final LocalDateTime testTime = LocalDateTime.of(2025, 07, 25, 00, 00, 00);

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final ItemRequest request = ItemRequest.builder()
            .id(1L)
            .requester(user1)
            .created(testTime)
            .description("desc1")
            .build();

    private final ShortItemDto item1 = ShortItemDto.builder()
            .id(1L)
            .name("item1")
            .description("desc1")
            .build();

    private final ShortItemDto item2 = ShortItemDto.builder()
            .id(2L)
            .name("item2")
            .description("desc2")
            .build();

    private final FullRequestDto fullRequest = FullRequestDto.builder()
            .id(request.getId())
            .description(request.getDescription())
            .created(request.getCreated())
            .items(Arrays.asList(item1, item2))
            .build();

    @Test
    void getRequestById_success() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(itemRepository.findByRequest_Id(request.getId())).thenReturn(Collections.emptyList());
        when(itemMapper.toShortItemDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(requestMapper.mapFullRequestDto(request, Collections.emptyList())).thenReturn(fullRequest);

        FullRequestDto result = requestService.getRequestById(request.getId(), user1.getId());

        assertNotNull(result);
        assertEquals(fullRequest.getId(), result.getId());
        assertEquals(fullRequest.getDescription(), result.getDescription());
        assertEquals(fullRequest.getItems().size(), result.getItems().size());

        verify(userRepository).findById(user1.getId());
        verify(requestRepository).findById(request.getId());
        verify(itemRepository).findByRequest_Id(request.getId());
        verify(requestMapper).mapFullRequestDto(request, Collections.emptyList());
    }

    @Test
    void getRequestByIdWithFailUser() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(request.getId(), user1.getId()));

        assertEquals("Не удалось найти пользователя с id " + request.getId(), ex.getMessage());
        verify(userRepository).findById(request.getId());
        verifyNoInteractions(requestRepository);
    }

    @Test
    void getRequestById_requestNotFound_throwsException() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(request.getId(), request.getId()));

        assertEquals("Не удалось найти запрос с id " + request.getId(), ex.getMessage());
        verify(userRepository).findById(request.getId());
        verify(requestRepository).findById(request.getId());
    }
}
