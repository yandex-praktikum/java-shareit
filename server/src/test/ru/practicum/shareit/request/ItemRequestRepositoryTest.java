package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findRequestByRequestorIdTest() {
        User user = new User(1L, "user1@email.com", "User1");
        user = userRepository.save(user);
        ItemRequest request = ItemRequest.builder()
                .id(0L)
                .created(LocalDateTime.now())
                .user(user)
                .description("request description")
                .build();

        request = requestRepository.save(request);

        TypedQuery<ItemRequest> query = em.getEntityManager()
                .createQuery("Select r from ItemRequest r where r.user.id = :id", ItemRequest.class);
        ItemRequest request1 = query.setParameter("id", request.getUser().getId()).getSingleResult();
        assertEquals(requestRepository.findRequestByRequestor_Id(request.getUser().getId()).get(0).getId(),
                request1.getId());
    }
}
