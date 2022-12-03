package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShareItTests {
    private final UserDao userDao;
    private final ItemDao itemDao;

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testCreateAndGetUser() {
        User user = new User(0L, "User1", "user1@gmail.com");
        userDao.createUser(user);

        assertThat(userDao.getUserById(1L).getName().equals("User1"));
    }

    @Test
    @Order(2)
    public void testUpdateUser() {
        User user = new User(0L, "User1", "update@gmail.com");
        userDao.updateUser(1L, user);

        assertThat(userDao.getUserById(1L).getEmail().equals("update@gmail.com"));
    }

    @Test
    @Order(3)
    public void testGetAllUsers() {
        assertThat(userDao.getAllUsers().size() == 1);
    }

    @Test
    @Order(4)
    public void testDeleteUser() {
        userDao.deleteUser(1l);
        assertThat(userDao.getAllUsers().size() == 0);
    }

    @Test
    @Order(5)
    public void testItemCreateAndGet() {
        User user = new User(0L, "User1", "user1@gmail.com");
        userDao.createUser(user);

        Item item = new Item(0L, "Item Vacuum", "Description vacuum", Boolean.TRUE, 1L);
        itemDao.createItem(item);
        assertThat(itemDao.getItemById(1L).getName().equals("Item Vacuum"));
    }

    @Test
    @Order(6)
    public void testGetAllItems() {
        assertThat(itemDao.getAllItems(1L).size() == 1);
    }

    @Test
    @Order(7)
    public void testUpdateItem() {
        Item item = new Item(0L, "Item Update", "Description vacuum", Boolean.TRUE, 1L);
        itemDao.updateItem(1L, item);
        assertThat(itemDao.getItemById(1L).getName().equals("Item Update"));
    }

    @Test
    @Order(8)
    public void testSearchItem() {
        assertThat(itemDao.searchItem("VacUUm").get(0).getName().equals("Item Update"));
    }
}
