package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRequestRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User requestAuthor;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;

    @BeforeEach
    void beforeEach() {
        requestAuthor = em.persistAndFlush(User.builder().name("name").email("e@mail.ru").build());
        itemRequest1 = em.persistAndFlush(ItemRequest.builder().description("хочу хорошую вещь")
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(25))
                .requestAuthor(requestAuthor).build());
        itemRequest2 = em.persistAndFlush(ItemRequest.builder().description("хочу другую хорошую вещь")
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusMinutes(20))
                .requestAuthor(requestAuthor).build());
    }

    @Test
    void findByRequestAuthor_idOrderByCreatedDescTest() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequestAuthor_idOrderByCreatedDesc(requestAuthor.getId());

        assertEquals(2, itemRequestList.size());
        assertEquals(itemRequest2.getRequestId(), itemRequestList.get(0).getRequestId());
        assertEquals(itemRequest1.getRequestId(), itemRequestList.get(1).getRequestId());
    }

    @Test
    void findByRequestAuthor_idNotOrderByCreatedDescTest() {
        Pageable page = PageRequest.of(0, 20);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findByRequestAuthor_idNotOrderByCreatedDesc(requestAuthor.getId(), page);

        assertEquals(0, itemRequestPage.getContent().size());
    }
}