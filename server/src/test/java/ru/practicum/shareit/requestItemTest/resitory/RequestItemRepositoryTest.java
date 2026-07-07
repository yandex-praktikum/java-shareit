package ru.practicum.shareit.requestItemTest.resitory;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserState;

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
class RequestItemRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Test
    void findAllByRequestorIdOrderByCreatedDesc_shouldReturnUserRequestsOrdered() {
        User user = User.builder()
                .name("Иван")
                .email("ivan@mail.com")
                .state(UserState.ACTIVE)
                .build();
        em.persist(user);
        em.flush();

        RequestItem request1 = new RequestItem();
        request1.setDescription("Нужна дрель");
        request1.setRequestor(user);
        request1.setCreated(Instant.now().minusSeconds(10));
        em.persist(request1);

        RequestItem request2 = new RequestItem();
        request2.setDescription("Нужна отвертка");
        request2.setRequestor(user);
        request2.setCreated(Instant.now());
        em.persist(request2);

        em.flush();

        List<RequestItem> result = requestItemRepository.findAllByRequestorIdOrderByCreatedDesc(user.getId());

        assertThat(result, hasSize(2));
        assertThat(result.get(0).getDescription(), is("Нужна отвертка"));
        assertThat(result.get(1).getDescription(), is("Нужна дрель"));
    }

    @Test
    void save_shouldGenerateId() {
        User user = User.builder()
                .name("Петр")
                .email("petr@mail.com")
                .state(UserState.ACTIVE)
                .build();
        em.persist(user);
        em.flush();

        RequestItem newRequest = new RequestItem();
        newRequest.setDescription("Новый запрос");
        newRequest.setRequestor(user);
        newRequest.setCreated(Instant.now());

        RequestItem saved = requestItemRepository.save(newRequest);

        assertThat(saved.getId(), notNullValue());
        assertThat(saved.getDescription(), is("Новый запрос"));
        assertThat(saved.getRequestor().getId(), is(user.getId()));
    }
}