package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.request.dto.ItemRequestDto;
import ru.practicum.shareit.item.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.item.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.requests.RequestTestData.*;


@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    @Autowired
    private final ItemRequestServiceImpl itemRequestService;

    @Test
    void testGetRequestById() {
        ItemRequestDto itemRequestDtoFromSQL = itemRequestService.getById(2L, 1L);
        assertThat(itemRequestDtoFromSQL, equalTo(itemRequestDto));
    }

    @Test
    void testGetRequestNotFound() {
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.getById(2L, 100L));
    }

    @Test
    @DirtiesContext
    void testCreate() {
        long requestId = itemRequestService.create(3L, requestDto).getId();
        ItemRequestDto itemRequestDtoFromSQL = itemRequestService.getById(3L, requestId);
        assertThat(itemRequestDtoFromSQL.getId(), equalTo(itemRequestDtoCreated.getId()));
        assertThat(itemRequestDtoFromSQL.getDescription(), equalTo(itemRequestDtoCreated.getDescription()));
    }

    @Test
    void testGetByRequestor() {
        List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getByRequestor(3L);
        assertThat(itemRequestDtoFromSQL, equalTo(List.of(itemRequestDto)));
    }

    @Test
    void testGetAll() {
        List<ItemRequestDto> itemRequestDtoFromSQL = itemRequestService.getAll(1L, 1, 10);
        assertThat(itemRequestDtoFromSQL, equalTo(List.of(itemRequestDto)));
    }

    @Test
    void testCheckUser() {
        assertThrows(UserNotFoundException.class, () -> itemRequestService.checkUser(200L));

    }


    @Test
    void testGetAllWrongFrom() {
        assertThrows(ItemRequestNotGoodParametrsException.class, () -> itemRequestService.getAll(1L, -1, 10));
    }
}
