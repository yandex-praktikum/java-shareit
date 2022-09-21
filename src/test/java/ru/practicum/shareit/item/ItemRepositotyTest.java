package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class ItemRepositotyTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testSearch() {
        Page<Item> items = itemRepository.search("item1", Pageable.ofSize(10));
        assertThat(items.stream().count(), equalTo(1L));
    }
}