package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void searchItemTest() {
        User user = new User(1L, "user1@email.com", "User1");
        user = userRepository.save(user);
        Item item = new Item(1L, "Item1", "drill for construction", true,
                user.getId(), null);
        item = itemRepository.save(item);

        List<Item> itemForSearch = itemRepository.searchItem("const");

        assertEquals(itemForSearch.get(0).getName(), item.getName());
    }
}
