package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User requestor;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        requestor = new User(null, "Requestor", "requestor@mail.com");
        requestor = userRepository.save(requestor);

        anotherUser = new User(null, "Another User", "another@mail.com");
        anotherUser = userRepository.save(anotherUser);
    }

    @Test
    void createRequest_whenValidData_thenRequestCreated() {
        ItemRequestDto requestDto = new ItemRequestDto(null, "Нужна дрель", null, null);

        ItemRequestDto result = itemRequestService.createRequest(requestDto, requestor.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Нужна дрель", result.getDescription());
        assertEquals(requestor.getId(), result.getRequestorId());
        assertNotNull(result.getCreated());
        assertNotNull(result.getItems());
    }

    @Test
    void getRequestsByRequestor_whenHasRequests_thenReturnSortedByDate() throws InterruptedException {
        ItemRequestDto request1 = itemRequestService.createRequest(
                new ItemRequestDto(null, "Первый запрос", null, null), requestor.getId());

        Thread.sleep(10);

        ItemRequestDto request2 = itemRequestService.createRequest(
                new ItemRequestDto(null, "Второй запрос", null, null), requestor.getId());

        List<ItemRequestDto> result = itemRequestService.getRequestsByRequestor(requestor.getId());

        assertEquals(2, result.size());
        assertEquals("Второй запрос", result.get(0).getDescription());
        assertEquals("Первый запрос", result.get(1).getDescription());
    }

    @Test
    void getAllRequests_whenOtherUsersHaveRequests_thenReturnPaginated() {
        ItemRequestDto request1 = itemRequestService.createRequest(
                new ItemRequestDto(null, "Запрос 1", null, null), anotherUser.getId());

        ItemRequestDto request2 = itemRequestService.createRequest(
                new ItemRequestDto(null, "Запрос 2", null, null), anotherUser.getId());

        ItemRequestDto request3 = itemRequestService.createRequest(
                new ItemRequestDto(null, "Запрос 3", null, null), anotherUser.getId());

        itemRequestService.createRequest(
                new ItemRequestDto(null, "Мой запрос", null, null), requestor.getId());

        List<ItemRequestDto> page1 = itemRequestService.getAllRequests(requestor.getId(), 0, 2);
        assertEquals(2, page1.size());

        List<ItemRequestDto> page2 = itemRequestService.getAllRequests(requestor.getId(), 2, 2);
        assertEquals(1, page2.size());

        assertTrue(page1.stream().noneMatch(r -> r.getDescription().equals("Мой запрос")));
        assertTrue(page2.stream().noneMatch(r -> r.getDescription().equals("Мой запрос")));
    }

    @Test
    void getRequestById_whenRequestHasItems_thenReturnWithItems() {
        ItemRequestDto request = itemRequestService.createRequest(
                new ItemRequestDto(null, "Нужна дрель для ремонта", null, null), requestor.getId());

        Item item = new Item(null, "Дрель", "Мощная дрель", true, anotherUser.getId(), request.getId());
        itemRepository.save(item);

        ItemRequestDto result = itemRequestService.getRequestById(request.getId(), requestor.getId());

        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
        assertEquals("Дрель", result.getItems().get(0).getName());
        assertEquals(anotherUser.getId(), result.getItems().get(0).getOwnerId());
    }

    @Test
    void getRequestById_whenRequestNotExists_thenThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.getRequestById(999L, requestor.getId()));
    }
}
