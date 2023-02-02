package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.utilities.PaginationConverter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemRequestUnitTests {

    private ItemRequestService requestService;
    private User user;
    private ItemRequest request;
    private ItemRequestDto requestDto;
    private Pageable pageable;
    private ItemRequestDtoResponse itemRequestDtoResponse;
    private Item item;
    private ItemDto itemDto;

    @Mock
    private ItemRequestRepository requestRepository;


    @Mock
    private UserRepository userRepository;

    @Mock
    private PaginationConverter paginationConverter;

    @Mock
    private ItemRequestMapper mapper;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        requestService = new ItemRequestServiceImpl(itemRepository, requestRepository, mapper,
                userRepository, paginationConverter);
        pageable = PageRequest.of(0, 1);
        user = User.builder()
                .id(1L)
                .email("user1@email.ru")
                .name("User1")
                .build();
        request = ItemRequest.builder()
                .id(1L)
                .description("desc")
                .user(user)
                .created(LocalDateTime.now())
                .build();
        requestDto = ItemRequestDto.builder()
                .requestorId(1L)
                .description("desc")
                .build();
        itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(request.getId())
                .items(null)
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(1L)
                .build();
    }

    @Test
    void addRequestTest() {

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(request);

        assertThat(requestService.addRequest(requestDto, 1L) != null);
    }

    @Test
    void findRequestByRequestorIdTest() {

        Mockito
                .when(requestRepository.findRequestByRequestor_Id(any()))
                .thenReturn(List.of(request));

        assertThat(requestRepository.findRequestByRequestor_Id(1L).size() == 1);
    }

    @Test
    void testFindAllRequests() {

        Mockito
                .when(paginationConverter.convert(any(), any(), any()))
                .thenReturn(pageable);

        assertThrows(UserNotFoundException.class,
                () -> requestService.getAllRequests(1L, 0, 1));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(requestService.getAllRequests(1L, 0, 1))
                .thenReturn(List.of(itemRequestDtoResponse));
        Pageable pg = paginationConverter.convert(0, 1, "id");

        assertThat(requestRepository.findAllRequests(1L, pg).size() == 1);
    }

    @Test
    void getRequestById() {

        assertThrows(UserNotFoundException.class,
                () -> requestService.getRequestByOwnerId(1L));

        assertThrows(UserNotFoundException.class,
                () -> requestService.getRequestById(1L, 1L));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(requestRepository.findRequestById(any()))
                .thenReturn(null);


        assertThrows(ItemRequestNotFoundException.class,
                () -> requestService.getRequestById(1L, 1L));

        Mockito
                .when(mapper.toRequestDtoForResponse(request))
                .thenReturn(itemRequestDtoResponse);
        Mockito
                .when(requestService.map(request))
                .thenReturn(itemRequestDtoResponse);

        Mockito
                .when(requestRepository.findRequestById(any()))
                .thenReturn(request);

        assertThat(requestService.getRequestById(1L, 1L) != null);


        assertThat(requestService.getRequestByOwnerId(1L).size() == 1);

    }

    @Test
    void allForCoverage() {
        assertThrows(UserNotFoundException.class,
                () -> requestService.addRequest(requestDto, 1L));

        Mockito
                .when(mapper.toRequestDtoForResponse(request))
                .thenReturn(itemRequestDtoResponse);
        Mockito
                .when(requestService.map(request))
                .thenReturn(itemRequestDtoResponse);

        assertThat(requestService.map(request).equals(itemRequestDtoResponse));

        assertThat(mapper.toRequestDtoForResponse(request).equals(itemRequestDtoResponse));

        Mockito
                .when(itemRepository.findItemsByRequestId(any()))
                .thenReturn(null);

        assertThrows(NullPointerException.class, () -> requestService.map(request));

        Mockito
                .when(itemRepository.findItemsByRequestId(any()))
                .thenReturn(List.of(item));

        assertThat(requestService.map(request).equals(itemRequestDtoResponse));

        Mockito
                .when(mapper.toRequest(any(), any()))
                .thenReturn(request);

        assertThat(mapper.toRequest(requestDto, user).getId().equals(request.getId()));

        Mockito
                .when(mapper.toRequestDtoForResponse(any()))
                .thenReturn(itemRequestDtoResponse);

        assertThat(mapper.toRequestDtoForResponse(request).getId().equals(request.getId()));
    }
}