package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.enums.BookerStatus;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.RequestCreateCommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.util.enums.ItemStatus;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemServiceImpl itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    private User user;
    private Item item1;
    private Item item2;

    @BeforeEach
    public void setUp() {
        user = new User(null, "testUser", "test@example.com");

        item1 = new Item(null, "Item 1", "Description 1", ItemStatus.AVAILABLE, user, null);
        item2 = new Item(null, "Item 2", "Description 2", ItemStatus.AVAILABLE, user, null);
    }

    @Test
    public void findAllItemsByUserId_shouldReturnItems() {
        Long userId = userRepository.save(user).getId();
        itemRepository.save(item1);
        itemRepository.save(item2);

        Collection<ItemDto> items = itemService.findAllItemsByUserId(userId);

        assertThat(items).isNotNull();
        assertThat(items).isNotEmpty();
        assertThat(items.size()).isEqualTo(2);
    }

    @Test
    public void findAllItemsByUserId_shouldReturnEmptyCollectionOfItems() {
        Collection<ItemDto> items = itemService.findAllItemsByUserId(999L);

        assertThat(items).isNotNull();
        assertThat(items).isEmpty();
    }

    @Test
    public void searchByNameAndDescription_shouldReturnItems() {
        userRepository.save(user);

        String substring = "&3276_substring_8291&";
        item1.setDescription(substring);
        item2.setDescription(substring);
        itemRepository.save(item1);
        itemRepository.save(item2);


        Collection<ItemDto> items = itemService.searchByNameAndDescription(substring);

        assertThat(items).isNotNull();
        assertThat(items).isNotEmpty();
        assertThat(items.size()).isEqualTo(2);
    }

    @Test
    public void searchByNameAndDescription_shouldReturnEmptyCollectionOfItems() {
        Collection<ItemDto> items = itemService.searchByNameAndDescription("WKDO-192K-FKWK-JS2J");

        assertThat(items).isNotNull();
        assertThat(items).isEmpty();
    }

    @Test
    public void saveCommentToItem_commentShouldBeSaved() {
        userRepository.save(user);
        Item storageItem = itemRepository.save(item1);
        Long itemId = storageItem.getId();

        User storageBooker = userRepository.save(new User(null, "Booker", "booker@gmail.com"));
        Long userId = storageBooker.getId();
        bookingRepository.save(
                new Booking(null,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        storageItem,
                        storageBooker,
                        BookerStatus.APPROVED));

        RequestCreateCommentDto requestCreateCommentDto = new RequestCreateCommentDto("Comment 1");

        CommentDto savedComment = itemService.saveCommentToItem(userId, itemId, requestCreateCommentDto);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getText()).isEqualTo(requestCreateCommentDto.getText());
        assertThat(savedComment.getAuthorName()).isEqualTo(storageBooker.getName());
    }

    @Test
    public void saveCommentToItem_shouldThrowNotFoundException() {
        userRepository.save(user);
        Item storageItem = itemRepository.save(item1);
        Long itemId = storageItem.getId();

        RequestCreateCommentDto requestCreateCommentDto = new RequestCreateCommentDto("Comment 1");

        assertThatThrownBy(() -> itemService.saveCommentToItem(999L, itemId, requestCreateCommentDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void saveCommentToItem_shouldThrowAccessDeniedException() {
        userRepository.save(user);
        Item storageItem = itemRepository.save(item1);
        Long itemId = storageItem.getId();

        User storageBooker = userRepository.save(new User(null, "Booker", "booker@gmail.com"));
        Long userId = storageBooker.getId();

        RequestCreateCommentDto requestCreateCommentDto = new RequestCreateCommentDto("Comment 1");

        assertThatThrownBy(() -> itemService.saveCommentToItem(userId, itemId, requestCreateCommentDto))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining(
                        format("User{id=%d} не может оставлять комментарии к Item{id=%d}", userId, itemId));
    }
}

