package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = new User(null, "Owner", "owner@mail.com");
        owner = userRepository.save(owner);

        booker = new User(null, "Booker", "booker@mail.com");
        booker = userRepository.save(booker);

        item = new Item(null, "Дрель", "Мощная дрель", true, owner.getId(), null);
        item = itemRepository.save(item);
    }

    @Test
    void getItemById_whenItemExists_thenReturnItemWithBookingsAndComments() {
        Booking booking = new Booking(null,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item.getId(),
                booker.getId(),
                BookingStatus.APPROVED);
        bookingRepository.save(booking);

        Comment comment = new Comment(null,
                "Отличная дрель!",
                item.getId(),
                booker.getId(),
                LocalDateTime.now());
        commentRepository.save(comment);

        ItemDto result = itemService.getItemById(item.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals("Дрель", result.getName());
        assertNotNull(result.getLastBooking());
        assertNotNull(result.getComments());
        assertEquals(1, result.getComments().size());
        assertEquals("Отличная дрель!", result.getComments().get(0).getText());
    }

    @Test
    void getItemsByOwner_whenOwnerHasItems_thenReturnAllItems() {
        Item item2 = new Item(null, "Молоток", "Тяжелый молоток", true, owner.getId(), null);
        itemRepository.save(item2);

        List<ItemDto> result = itemService.getItemsByOwner(owner.getId());

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(i -> i.getName().equals("Дрель")));
        assertTrue(result.stream().anyMatch(i -> i.getName().equals("Молоток")));
    }

    @Test
    void searchItems_whenTextMatches_thenReturnAvailableItems() {
        Item unavailableItem = new Item(null, "Дрель аккумуляторная", "Нет батареи", false, owner.getId(), null);
        itemRepository.save(unavailableItem);

        List<ItemDto> result = itemService.searchItems("дрель", booker.getId());

        assertEquals(1, result.size());
        assertEquals("Дрель", result.get(0).getName());
        assertTrue(result.get(0).getAvailable());
    }

    @Test
    void createItem_whenValidData_thenItemCreated() {
        ItemDto newItemDto = new ItemDto(null, "Пила", "Цепная пила", true, null);

        ItemDto result = itemService.createItem(newItemDto, owner.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Пила", result.getName());
        assertEquals("Цепная пила", result.getDescription());
        assertTrue(result.getAvailable());

        Item savedItem = itemRepository.findById(result.getId()).orElseThrow();
        assertEquals(owner.getId(), savedItem.getOwnerId());
    }

    @Test
    void updateItem_whenOwnerUpdates_thenItemUpdated() {
        ItemDto updateDto = new ItemDto(null, "Дрель Updated", "Обновленное описание", false, null);

        ItemDto result = itemService.updateItem(item.getId(), updateDto, owner.getId());

        assertNotNull(result);
        assertEquals("Дрель Updated", result.getName());
        assertEquals("Обновленное описание", result.getDescription());
        assertFalse(result.getAvailable());

        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertEquals("Дрель Updated", updatedItem.getName());
        assertEquals("Обновленное описание", updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
    }

    @Test
    void updateItem_whenNotOwner_thenThrowException() {
        ItemDto updateDto = new ItemDto(null, "Новое название", null, null, null);

        assertThrows(EntityNotFoundException.class,
                () -> itemService.updateItem(item.getId(), updateDto, booker.getId()));
    }
}
