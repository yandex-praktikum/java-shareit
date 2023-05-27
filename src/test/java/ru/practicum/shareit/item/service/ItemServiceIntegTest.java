package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegTest {
    private final ItemService itemService;
    private final UserService userService;

    @BeforeEach
    public void addSomeItems() {
        UserDto author = new UserDto(null, "sabrina@email.ru", "Sabrina");
        UserDto newUser = userService.addUser(author);

        ItemDto newItem1 = new ItemDto(2, "плед", "теплый", true, null, null, null, null);
        itemService.addItem(newUser.getId(), newItem1);
        ItemDto newItem2 = new ItemDto(2, "палатка", "большая, для похода", true, null, null, null, null);
        itemService.addItem(newUser.getId(), newItem2);
    }

    @Test
    public void shouldSuccessGetByText() {
        List<ItemDto> itemByText = itemService.getItems("для похода");
        Assertions.assertNotNull(itemByText);
        Assertions.assertEquals(itemByText.get(0).getName(), "палатка");
    }
}
