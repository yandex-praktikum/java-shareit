package shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepository;

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final User user2 = User.builder()
            .id(2L)
            .name("user2")
            .email("email2@ya.ru")
            .build();

    private final Item item1 = Item.builder()
            .id(1L)
            .name("item1")
            .description("description1")
            .available(true)
            .owner(user1)
            .build();

    private final Item item2 = Item.builder()
            .id(2L)
            .name("item2")
            .description("description2")
            .available(true)
            .owner(user2)
            .build();

    private final Item item3 = Item.builder()
            .id(3L)
            .name("name3")
            .description("description3")
            .available(false)
            .owner(user1)
            .build();

    private final List<Item> items = List.of(item1, item3);

    @BeforeEach
    public void init() {
        userRepository.save(user1);
        userRepository.save(user2);
        repository.save(item1);
        repository.save(item2);
        repository.save(item3);
    }

    private void checkItem(Item item1, Item item2) {
        assertEquals(item1.getId(), item2.getId(), "Не совпадает id");
        assertEquals(item1.getName(), item2.getName(), "Не совпадает наименование");
        assertEquals(item1.getDescription(), item2.getDescription(), "Не совпадает описание");
        assertEquals(item1.getAvailable(), item2.getAvailable(), "Не совпадает статус");
        assertEquals(item1.getOwner().getId(), item2.getOwner().getId(), "Не совпадает id владельца");
        assertEquals(item1.getOwner().getName(), item2.getOwner().getName(), "Не совпадает имя владельца");
        assertEquals(item1.getOwner().getEmail(), item2.getOwner().getEmail(), "Не совпадает почта владельца");
    }

    @Test
    public void findByOwnerTest() {
        List<Item> itemsR = repository.findByOwner(user1); //item1, item 3

        assertEquals(itemsR.size(), 2, "Количество вещей не совпадает");
        checkItem(itemsR.get(0), items.get(0));
        checkItem(itemsR.get(1), items.get(1));
    }

    @Test
    public void findByOwnerIdTest() {
        List<Item> itemsR = repository.findByOwnerId(user1.getId()); //item1, item 3

        assertEquals(itemsR.size(), 2, "Количество вещей не совпадает");
        checkItem(itemsR.get(0), items.get(0));
        checkItem(itemsR.get(1), items.get(1));
    }

    @Test
    public void findByRequestIdTest() {

    }

    @Test
    public void searchItemsTest() {
        List<Item> itemsR = repository.searchItems("2");
        assertEquals(itemsR.size(), 1, "Количество вещей не совпадает");
        checkItem(itemsR.get(0), item2);
    }
}
