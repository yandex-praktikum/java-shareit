package ru.practicum.shareit.item.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
public class ItemRepoTest {
    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        User owner = new User();
        owner.setEmail("ppp@email.ru");
        owner.setName("Polina");
        userRepository.save(owner);
    }

    @Test
    public void shouldReturnAddedItem() {
        Item item = new Item();
        item.setName("Коньки");
        item.setDescription("для катания");
        item.setIsAvailable(true);
        User owner = userRepository.findById(1).isPresent() ? userRepository.findById(1).get() : null;
        item.setOwner(owner);

        repository.save(item);

        Item newItem = repository.findById(1).isPresent() ? repository.findById(1).get() : null;

        Assertions.assertNotNull(newItem);
        Assertions.assertEquals(newItem.getName(), item.getName());
    }
}
