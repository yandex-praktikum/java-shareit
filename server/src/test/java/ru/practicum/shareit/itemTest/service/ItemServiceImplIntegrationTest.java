package ru.practicum.shareit.itemTest.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserState;
import ru.practicum.shareit.user.repository.UserRepository;

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
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    void createItem_shouldSaveAndReturnItem() {
        User owner = User.builder()
                .name("Иван")
                .email("ivan@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Дрель");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(true);

        Item result = itemService.createItem(item, owner.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), is("Дрель"));
        assertThat(result.getDescription(), is("Аккумуляторная дрель"));
        assertThat(result.getAvailable(), is(true));
        assertThat(result.getOwner().getId(), is(owner.getId()));

        Item savedItem = itemRepository.findById(result.getId()).orElse(null);
        assertThat(savedItem, notNullValue());
        assertThat(savedItem.getName(), is("Дрель"));
    }

    @Test
    void updateItem_shouldUpdateExistingItem() {
        User owner = User.builder()
                .name("Петр")
                .email("petr@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Старая дрель");
        item.setDescription("Старое описание");
        item.setAvailable(false);
        item.setOwner(owner);
        itemRepository.save(item);

        Item updateData = new Item();
        updateData.setName("Новая дрель");
        updateData.setDescription("Новое описание");
        updateData.setAvailable(true);

        Item result = itemService.updateItem(updateData, item.getId(), owner.getId());

        assertThat(result.getName(), is("Новая дрель"));
        assertThat(result.getDescription(), is("Новое описание"));
        assertThat(result.getAvailable(), is(true));

        Item updatedItem = itemRepository.findById(item.getId()).orElse(null);
        assertThat(updatedItem.getName(), is("Новая дрель"));
    }

    @Test
    void findItemById_shouldReturnItem() {
        User owner = User.builder()
                .name("Сергей")
                .email("sergey@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Молоток");
        item.setDescription("Тяжелый молоток");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Item result = itemService.findItemById(item.getId());

        assertThat(result.getId(), is(item.getId()));
        assertThat(result.getName(), is("Молоток"));
        assertThat(result.getDescription(), is("Тяжелый молоток"));
        assertThat(result.getAvailable(), is(true));
    }

    @Test
    void findAllItemsByUser_shouldReturnUserItems() {
        User owner = User.builder()
                .name("Анна")
                .email("anna@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        Item item1 = new Item();
        item1.setName("Вещь 1");
        item1.setDescription("Описание 1");
        item1.setAvailable(true);
        item1.setOwner(owner);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Вещь 2");
        item2.setDescription("Описание 2");
        item2.setAvailable(false);
        item2.setOwner(owner);
        itemRepository.save(item2);

        List<Item> result = itemService.findAllItemsByUser(owner.getId());

        assertThat(result, hasSize(2));
    }

    @Test
    void searchItems_shouldReturnOnlyAvailableItems() {
        User owner = User.builder()
                .name("Ольга")
                .email("olga@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        Item availableItem = new Item();
        availableItem.setName("Дрель аккумуляторная");
        availableItem.setDescription("Отличная дрель");
        availableItem.setAvailable(true);
        availableItem.setOwner(owner);
        itemRepository.save(availableItem);

        Item notAvailableItem = new Item();
        notAvailableItem.setName("Дрель сетевая");
        notAvailableItem.setDescription("Старая дрель");
        notAvailableItem.setAvailable(false);
        notAvailableItem.setOwner(owner);
        itemRepository.save(notAvailableItem);

        em.flush();

        List<Item> result = itemService.searchItems("дрель");

        assertThat(result, hasSize(1));
    }

    @Test
    void deleteItem_shouldRemoveItem() {
        User owner = User.builder()
                .name("Дмитрий")
                .email("dmitry@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Удаляемая вещь");
        item.setDescription("Будет удалена");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        itemService.deleteItem(item.getId(), owner.getId());

        Item deletedItem = itemRepository.findById(item.getId()).orElse(null);
        assertThat(deletedItem, nullValue());
    }
}
