package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final EntityManager em;
    private final ItemRequestService service;
    private final ItemRequestMapper mapper;
    private ItemRequestDto itemRequestCreatedExpected;
    private ItemRequestDto itemRequestExistedExpected;

    private static final int NON_EXISTENT_ID = 999;

    @BeforeEach
    public void testInit() {
        itemRequestCreatedExpected = new ItemRequestDto();
        itemRequestCreatedExpected.setId(1);
        itemRequestCreatedExpected.setDescription("description");
        itemRequestExistedExpected = new ItemRequestDto();
        itemRequestExistedExpected.setId(10);
        itemRequestExistedExpected.setDescription("description1");
    }

    @Test
    void createItemRequestTest() {
        int userId = 10;
        ItemRequestSaveDto itemRequest = new ItemRequestSaveDto();
        itemRequest.setDescription(itemRequestCreatedExpected.getDescription());
        int itemRequestExpectedId = itemRequestCreatedExpected.getId();

        service.createItemRequest(userId, itemRequest);
        TypedQuery<ItemRequest> query =
                em.createQuery("SELECT r FROM ItemRequest AS r WHERE r.id = :id", ItemRequest.class);
        ItemRequestDto itemRequestCreated = mapper
                .map(query.setParameter("id", itemRequestExpectedId).getSingleResult());

        assertThat(itemRequestCreated, allOf(
                hasProperty("id", equalTo(itemRequestCreatedExpected.getId())),
                hasProperty("description", equalTo(itemRequestCreatedExpected.getDescription()))
        ));
    }

    @Test
    void createItemRequestWithNonExistentUserTest() {
        ItemRequestSaveDto itemRequest = new ItemRequestSaveDto();
        itemRequest.setDescription(itemRequestCreatedExpected.getDescription());
        assertThrows(UserNotFoundException.class, () -> service.createItemRequest(NON_EXISTENT_ID, itemRequest));
    }

    @Test
    void getAllUserItemRequestTest() {
        int userId = itemRequestExistedExpected.getId();

        List<ItemRequestDto> itemRequests = service.getAllUserItemRequest(userId)
                .stream()
                .toList();
        assertEquals(itemRequests.size(), 1);
        assertThat(itemRequests.getFirst(), allOf(
                hasProperty("id", equalTo(itemRequestExistedExpected.getId())),
                hasProperty("description", equalTo(itemRequestExistedExpected.getDescription()))
        ));
    }

    @Test
    void getAllItemRequestsTest() {
        int itemRequestExpectedId = 10;
        Collection<ItemRequestDto> itemRequests = service.getAllItemRequests();
        assertEquals(itemRequests.size(), 5);
        for (ItemRequestDto request : itemRequests) {
            assertThat(request, allOf(
                    hasProperty("id", equalTo(itemRequestExpectedId)),
                    hasProperty("description", equalTo("description" + itemRequestExpectedId / 10))
            ));
            itemRequestExpectedId += 10;
        }
    }

    @Test
    void getItemRequestTest() {
        int itemRequestId = itemRequestExistedExpected.getId();
        ItemRequestDto itemRequest = service.getItemRequest(itemRequestId);
        assertThat(itemRequest, allOf(
                hasProperty("id", equalTo(itemRequestExistedExpected.getId())),
                hasProperty("description", equalTo(itemRequestExistedExpected.getDescription()))
        ));
    }
}