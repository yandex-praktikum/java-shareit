package ru.practicum.shareit.itemTest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceUnitTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private RequestItemRepository requestItemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void createItem_shouldSaveAndReturnItem() {
        User owner = new User();
        owner.setId(1L);

        Item itemToSave = new Item();
        itemToSave.setName("Дрель");
        itemToSave.setDescription("Аккумуляторная");
        itemToSave.setAvailable(true);

        Item savedItem = new Item();
        savedItem.setId(1L);
        savedItem.setName("Дрель");
        savedItem.setDescription("Аккумуляторная");
        savedItem.setAvailable(true);
        savedItem.setOwner(owner);

        when(userService.getUserById(1L)).thenReturn(owner);
        when(itemRepository.save(itemToSave)).thenReturn(savedItem);

        Item result = itemService.createItem(itemToSave, 1L);

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("Дрель"));
        assertThat(result.getAvailable(), is(true));
        verify(itemRepository, times(1)).save(itemToSave);
    }

    @Test
    void updateItem_shouldUpdate_whenOwnerMatches() {
        Item newItemData = new Item();
        newItemData.setName("Новое имя");

        User owner = new User();
        owner.setId(1L);

        Item existingItem = new Item();
        existingItem.setId(1L);
        existingItem.setName("Старое имя");
        existingItem.setOwner(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(existingItem)).thenReturn(existingItem);

        Item result = itemService.updateItem(newItemData, 1L, 1L);

        assertThat(result.getName(), is("Новое имя"));
    }

    @Test
    void updateItem_shouldThrowAccessDenied_whenOwnerNotMatches() {
        User owner = new User();
        owner.setId(1L);

        Item existingItem = new Item();
        existingItem.setId(1L);
        existingItem.setOwner(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));

        assertThatThrownBy(() -> itemService.updateItem(new Item(), 1L, 2L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Редактировать можно только свои вещи");
    }

    @Test
    void findItemById_shouldThrowNotFound_whenItemNotExists() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.findItemById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найдена");
    }

    @Test
    void deleteItem_shouldDelete_whenOwnerMatches() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(1L);

        itemService.deleteItem(1L, 1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteItem_shouldThrowAccessDenied_whenOwnerNotMatches() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> itemService.deleteItem(1L, 2L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Удалять можно только свои вещи");
    }
}