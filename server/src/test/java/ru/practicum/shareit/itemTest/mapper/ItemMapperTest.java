package ru.practicum.shareit.itemTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.mapper.ItemMapperMapstruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemRequestDto;
import ru.practicum.shareit.item.model.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class ItemMapperTest {

    private User owner;
    private ItemRequestDto dto;
    private Item item;
    private ItemMapperMapstruct itemMapperMapstruct;  // ← Поле

    @BeforeEach
    public void setUp() {
        itemMapperMapstruct = Mappers.getMapper(ItemMapperMapstruct.class);

        owner = new User();
        owner.setId(1L);
        owner.setName("Вася");
        owner.setEmail("vasya@ya.ru");

        dto = new ItemRequestDto();
        dto.setName("Дрель");
        dto.setDescription("Аккумуляторная, 12V");
        dto.setAvailable(true);

        item = new Item();
        item.setId(1L);
        item.setName("Старая дрель");
        item.setDescription("Старое описание");
        item.setAvailable(false);
        item.setOwner(owner);
    }

    @Test
    public void shouldCreateItemFromDtoAndOwner() {
        Item item = itemMapperMapstruct.mapToItem(dto, owner);

        assertNotNull(item);
        assertNull(item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Аккумуляторная, 12V", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
    }

    @Test
    public void shouldCreateDtoFromItem() {
        ItemResponseDto dto = itemMapperMapstruct.mapToDto(item);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Старая дрель", dto.getName());
        assertEquals("Старое описание", dto.getDescription());
        assertFalse(dto.getAvailable());
        assertEquals(1L, dto.getOwnerId());
    }

    @Test
    public void shouldUpdateItemFromDto() {
        itemMapperMapstruct.updateItemFromDto(dto, item);

        assertEquals(1L, item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Аккумуляторная, 12V", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(1L, item.getOwner().getId());
    }
}