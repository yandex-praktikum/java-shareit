package ru.practicum.shareit.requestItemTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.request.service.RequestItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserState;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
class RequestItemServiceImplIntegrationTest {

    @Autowired
    private RequestItemService requestItemService;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createRequestItem_shouldSaveAndReturnRequest() {
        User requestor = User.builder()
                .name("Иван")
                .email("ivan@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(requestor);

        RequestItem item = RequestItem.builder()
                .description("Нужна аккумуляторная дрель")
                .build();

        RequestItem result = requestItemService.createRequestItem(item, requestor.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), is("Нужна аккумуляторная дрель"));
        assertThat(result.getRequestor().getId(), is(requestor.getId()));
        assertThat(result.getCreated(), notNullValue());

        RequestItem saved = requestItemRepository.findById(result.getId()).orElse(null);
        assertThat(saved, notNullValue());
        assertThat(saved.getDescription(), is("Нужна аккумуляторная дрель"));
    }

    @Test
    void getAllRequestItemByOwner_shouldReturnUserRequests() {
        User requestor = User.builder()
                .name("Петр")
                .email("petr@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(requestor);

        RequestItem request1 = RequestItem.builder()
                .description("Нужна дрель")
                .requestor(requestor)
                .created(Instant.now().minusSeconds(10))
                .build();
        requestItemRepository.save(request1);

        RequestItem request2 = RequestItem.builder()
                .description("Нужна отвертка")
                .requestor(requestor)
                .created(Instant.now())
                .build();
        requestItemRepository.save(request2);

        List<RequestItem> result = requestItemService.getAllRequestItemByOwner(requestor.getId());

        assertThat(result, hasSize(2));
    }

    @Test
    void getAllRequestItemAllUsers_shouldReturnOtherUsersRequests() {
        User currentUser = User.builder()
                .name("Текущий")
                .email("current@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(currentUser);

        User otherUser = User.builder()
                .name("Другой")
                .email("other@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(otherUser);

        RequestItem currentRequest = RequestItem.builder()
                .description("Мой запрос")
                .requestor(currentUser)
                .created(Instant.now())
                .build();
        requestItemRepository.save(currentRequest);

        RequestItem otherRequest = RequestItem.builder()
                .description("Запрос другого")
                .requestor(otherUser)
                .created(Instant.now())
                .build();
        requestItemRepository.save(otherRequest);

        List<RequestItem> result = requestItemService.getAllRequestItemAllUsers(currentUser.getId());

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getDescription(), is("Запрос другого"));
    }

    @Test
    void getRequestItemById_shouldReturnRequest() {
        User requestor = User.builder()
                .name("Сергей")
                .email("sergey@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(requestor);

        RequestItem request = RequestItem.builder()
                .description("Нужен молоток")
                .requestor(requestor)
                .created(Instant.now())
                .build();
        requestItemRepository.save(request);

        RequestItem result = requestItemService.getRequestItemById(request.getId());

        assertThat(result.getId(), is(request.getId()));
        assertThat(result.getDescription(), is("Нужен молоток"));
        assertThat(result.getRequestor().getId(), is(requestor.getId()));
    }
}