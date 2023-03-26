package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestServiceTest {

    @Test
    void getItemRequestByIdNormalWay() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new ItemRequest(1,
                        new User(1, "name", "e@mail.ru"),
                        "описание запроса",
                        LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                        List.of())));

        ItemRequest itemRequest = itemRequestService.getById(1, 1);

        Assertions.assertEquals(1, itemRequest.getRequestId());
        Assertions.assertEquals(new User(1, "name", "e@mail.ru"), itemRequest.getRequestAuthor());
        Assertions.assertEquals("описание запроса", itemRequest.getDescription());
        Assertions.assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), itemRequest.getCreated());
    }

    @Test
    void getItemRequestByIdUserNotFound() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.getById(1, 1));

        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    void getItemRequestByIdItemRequestNotFound() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Mockito
                .when(mockItemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemRequestNotFoundException exception = Assertions.assertThrows(
                ItemRequestNotFoundException.class,
                () -> itemRequestService.getById(1, 1));

        Assertions.assertEquals("Запрос на вещь 1 не найден", exception.getMessage());
    }

    @Test
    void getOwnItemRequestsNormalWay() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Mockito
                .when(mockItemRequestRepository.findByRequestAuthor_idOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(List.of(
                        new ItemRequest(1,
                                new User(1, "name1", "e@mail1.ru"),
                                "описание запроса 1",
                                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                                List.of()),

                        new ItemRequest(2,
                                new User(2, "name2", "e@mail2.ru"),
                                "описание запроса 2",
                                LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.HOURS),
                                List.of())
                ));

        List<ItemRequest> itemRequests = itemRequestService.getOwnItemRequests(1);

        Assertions.assertEquals(2, itemRequests.size());
        Assertions.assertEquals(1, itemRequests.get(0).getRequestId());
        Assertions.assertEquals(2, itemRequests.get(1).getRequestId());
    }

    @Test
    void getOwnItemRequestsUserNotFound() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.getOwnItemRequests(1));

        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    void createItemRequestNormalWay() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Mockito
                .when(mockItemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(new ItemRequest(1,
                        new User(1, "name", "e@mail.ru"),
                        "описание запроса",
                        LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                        List.of()));

        ItemRequest itemRequest = itemRequestService.create(ItemRequest.builder()
                        .description("описание запроса").build(),
                1);

        assertEquals(1, itemRequest.getRequestId());
        assertEquals(new User(1, "name", "e@mail.ru"), itemRequest.getRequestAuthor());
        assertEquals("описание запроса", itemRequest.getDescription());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS), itemRequest.getCreated());
    }

    @Test
    void createItemRequestUserNotFound() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemRequestService.create(new ItemRequest(), 1));

        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    void getAllTest() {
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockUserRepository);

        ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        ItemRequestService itemRequestService = new ItemRequestService(mockItemRequestRepository, userService);

        ItemRequest itemRequest = new ItemRequest(1,
                new User(1, "name", "e@mail.ru"),
                "описание запроса",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS),
                List.of());
        Page<ItemRequest> itemRequestPage = new PageImpl<>(List.of(itemRequest));

        Mockito
                .when(mockItemRequestRepository.findByRequestAuthor_idNotOrderByCreatedDesc(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(itemRequestPage);

        List<ItemRequest> itemRequestList = itemRequestService.getAll(0, 20, 1);

        assertEquals(1, itemRequestList.size());
        assertEquals(1, itemRequestList.get(0).getRequestId());
    }
}