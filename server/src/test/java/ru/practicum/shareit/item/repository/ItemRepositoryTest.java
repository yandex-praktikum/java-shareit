package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User(1, "User1", "user1@mail.ru");
        User user2 = new User(2, "User2", "user2@mail.ru");
        ItemRequest itemRequest1 = new ItemRequest("description1", user2, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest("description2", user1, LocalDateTime.now());
        Item item1 = new Item(1, "item1", "description1", true, user1, itemRequest1);
        Item item2 = new Item(2, "item2", "meow", true, user2, itemRequest2);

        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    void findByOwnerIdTest() throws Exception {
        PageRequest page = PageRequest.of(0, 2);
        List<Item> test = itemRepository.findByOwnerId(1, page);

        Assertions.assertEquals(1, test.size());
        Assertions.assertEquals("item1", test.get(0).getName());
        Assertions.assertEquals("description1", test.get(0).getDescription());
        Assertions.assertEquals("User1", test.get(0).getOwner().getName());
        Assertions.assertEquals("description1", test.get(0).getRequest().getDescription());
    }

    @Test
    void findSearchTest() throws Exception {
        PageRequest page = PageRequest.of(0, 2);
        List<Item> test = itemRepository.findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue("meow", "meow", page);

        Assertions.assertEquals(1, test.size());
        Assertions.assertEquals("item2", test.get(0).getName());
        Assertions.assertEquals("meow", test.get(0).getDescription());
        Assertions.assertEquals("User2", test.get(0).getOwner().getName());
        Assertions.assertEquals("description2", test.get(0).getRequest().getDescription());
    }

    @Test
    void findAllByRequestIdTest() throws Exception {
        List<Item> test = itemRepository.findAllByRequestId(2);

        Assertions.assertEquals(1, test.size());
        Assertions.assertEquals("item2", test.get(0).getName());
        Assertions.assertEquals("meow", test.get(0).getDescription());
        Assertions.assertEquals("User2", test.get(0).getOwner().getName());
        Assertions.assertEquals("description2", test.get(0).getRequest().getDescription());
    }
}
