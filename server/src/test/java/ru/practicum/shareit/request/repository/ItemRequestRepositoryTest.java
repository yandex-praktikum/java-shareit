package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import org.junit.jupiter.api.Assertions;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {

    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void findByRequesterIdTest() throws Exception {
        User user1 = new User(1, "User1", "user1@mail.ru");
        User user2 = new User(2, "User2", "user2@mail.ru");
        ItemRequest itemRequest1 = new ItemRequest("description", user1, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest("description", user2, LocalDateTime.now());

        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        List<ItemRequest> test = itemRequestRepository.findByRequesterId(1);
        Assertions.assertEquals(1, test.size());
        Assertions.assertEquals("User1", test.get(0).getRequester().getName());
        Assertions.assertNotEquals("User2", test.get(0).getRequester().getName());
        Assertions.assertEquals("user1@mail.ru", test.get(0).getRequester().getEmail());
    }

    @Test
    void findAllByRequesterIdNotTest() throws Exception {
        User user1 = new User(1, "User1", "user1@mail.ru");
        User user2 = new User(2, "User2", "user2@mail.ru");
        ItemRequest itemRequest1 = new ItemRequest("description1", user1, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest("description2", user2, LocalDateTime.now());
        PageRequest page = PageRequest.of(0, 2);

        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        List<ItemRequest> test = itemRequestRepository.findAllByRequesterIdNot(1, page).getContent();

        Assertions.assertEquals(1, test.size());
        Assertions.assertEquals("User2", test.get(0).getRequester().getName());
        Assertions.assertNotEquals("User1", test.get(0).getRequester().getName());
        Assertions.assertEquals("user2@mail.ru", test.get(0).getRequester().getEmail());
        Assertions.assertEquals("description2", test.get(0).getDescription());
    }
}
