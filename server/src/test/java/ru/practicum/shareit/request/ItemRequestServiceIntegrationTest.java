package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(
        properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {

    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    void getOwnItemRequests() {
        User requestUser = userService.create(new User(0, "request user name", "request@user.org"));

        ItemRequest itemRequest1 = itemRequestService.create(
                ItemRequest.builder()
                        .description("Хочу определенную вещь")
                        .build(),
                requestUser.getId());

        ItemRequest itemRequest2 = itemRequestService.create(
                ItemRequest.builder()
                        .description("Хочу другую вещь")
                        .build(),
                requestUser.getId());

        List<ItemRequest> itemRequestList = itemRequestService.getOwnItemRequests(requestUser.getId());

        assertEquals(2, itemRequestList.size());
        assertEquals(itemRequest2.getRequestId(), itemRequestList.get(0).getRequestId());
        assertEquals(itemRequest1.getRequestId(), itemRequestList.get(1).getRequestId());
    }
}