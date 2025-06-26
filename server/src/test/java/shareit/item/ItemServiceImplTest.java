package shareit.item;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Spy
    private final ItemMapper mapper = Mappers.getMapper(ItemMapper.class);

    @Spy
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Spy
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @InjectMocks
    private ItemServiceImpl service;

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final Item item1 = Item.builder()
            .id(1L)
            .owner(user1)
            .name("item1")
            .description("desc1")
            .available(true)
            .build();

    private final Item item2 = Item.builder()
            .id(2L)
            .owner(user1)
            .name("item2")
            .description("desc2")
            .available(true)
            .build();

    private final Comment comment1 = Comment.builder()
            .id(1L)
            .item(item1)
            .text("text1")
            .createdAt(LocalDateTime.now())
            .author(user1)
            .build();

    private final Booking booking1 = Booking.builder()
            .id(1L)
            .item(item1)
            .booker(user1)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now())
            .build();

    private void checkItem(ItemDto dto1, ItemDto dto2) {
        assertEquals(dto1.getId(), dto2.getId());
        assertEquals(dto1.getName(), dto2.getName());
        assertEquals(dto1.getDescription(), dto2.getDescription());
        assertEquals(dto1.getAvailable(), dto2.getAvailable());
        assertEquals(dto1.getOwner(), dto2.getOwner());
    }

    @Nested
    class createItem {

        @Test
        public void createItem() {
            when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
            when(repository.save(any())).thenReturn(item1);
            ItemDto dto = mapper.mapItemDto(item1);

            ItemDto result = service.createItem(user1.getId(), dto);

            verify(repository, times(1)).save(any());
            checkItem(dto, result);
        }

        @Test
        public void createItemWithFailUser() {
            Long userId = 99L;
            ItemDto dto = mapper.mapItemDto(item1);

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> service.createItem(userId, dto));

            assertEquals("Не удалось найти пользователя с id 99", ex.getMessage());
            verify(repository, never()).save(any());
        }

        //TODO
        @Test
        public void createItemWithRequest() {

        }
    }

    @Nested
    class getItem {
        @Test
        public void getItemById() {
            when(repository.findById(item1.getId())).thenReturn(Optional.of(item1));
            when(commentRepository.findCommentsByItemId(item1.getId())).thenReturn(Collections.EMPTY_LIST);
            when(bookingRepository.findLastBooking(anyLong(), any())).thenReturn(Optional.empty());
            when(bookingRepository.findNextBooking(anyLong(), any())).thenReturn(Optional.empty());
            ItemDto dto = mapper.mapItemDto(item1);

            ItemWithCommentDto result = service.getItem(item1.getId());

            verify(repository, times(1)).findById(item1.getId());

            assertEquals(dto.getId(), result.getId());
            assertEquals(dto.getName(), result.getName());
            assertEquals(dto.getDescription(), result.getDescription());
            assertEquals(dto.getAvailable(), result.getAvailable());
            assertEquals(dto.getOwner(), result.getOwner());
        }
    }

    @Test
    public void getUserItems() {
        List<Item> items = List.of(item1, item2);
        List<Long> itemIds = items.stream().map(Item::getId).toList();

        CommentDto commentDto = commentMapper.mapCommentDto(comment1);

        when(repository.findByOwnerId(user1.getId())).thenReturn(items);
        when(repository.findByOwner(user1)).thenReturn(items);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(commentRepository.findCommentByItemIdIn(itemIds)).thenReturn(List.of(comment1));
        when(bookingRepository.findLastBooking(eq(item1.getId()), any())).thenReturn(Optional.of(booking1));
        when(bookingRepository.findNextBooking(eq(item1.getId()), any())).thenReturn(Optional.empty());
        when(bookingRepository.findLastBooking(eq(item2.getId()), any())).thenReturn(Optional.empty());
        when(bookingRepository.findNextBooking(eq(item2.getId()), any())).thenReturn(Optional.empty());

        List<ItemWithCommentDto> result = service.getUserItems(user1.getId());

        assertEquals(2, result.size());

        ItemWithCommentDto result1 = result.stream()
                .filter(dto -> dto.getId().equals(item1.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(item1.getName(), result1.getName());
        assertEquals(1, result1.getComments().size());
        assertEquals(comment1.getText(), result1.getComments().get(0).getText());

        assertNotNull(result1.getLastBooking());
        assertEquals(booking1.getId(), result1.getLastBooking().getId());

        assertNull(result1.getNextBooking());

        ItemWithCommentDto result2 = result.stream()
                .filter(dto -> dto.getId().equals(item2.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(item2.getName(), result2.getName());
        assertTrue(result2.getComments().isEmpty());
        assertNull(result2.getLastBooking());
        assertNull(result2.getNextBooking());

        verify(repository, times(1)).findByOwnerId(user1.getId());
        verify(repository, times(1)).findByOwner(user1);
        verify(commentRepository, times(1)).findCommentByItemIdIn(itemIds);
    }
}
