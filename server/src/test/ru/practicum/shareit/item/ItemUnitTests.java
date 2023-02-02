package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.Status;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.exception.CommentAddException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.NoRightsToUpdateException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.ItemRequestRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemUnitTests {
    private ItemService itemService;
    private Item item;
    private ItemDto itemDto;
    private Booking booking;
    private CommentDto commentDto;
    private Comment comment;
    private User user;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository requestRepository;


    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository,
                bookingRepository, requestRepository);
        user = new User(1L, "User1", "user1@mail.com");

        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(1L)
                .build();

        User user = new User(1L, "User1", "user1@mail.com");

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().minusHours(5))
                .end(LocalDateTime.now().minusHours(1))
                .bookerId(1L)
                .status(Status.WAITING)
                .build();

        booking = new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                item, user, bookingDto.getStatus());
        commentDto = new CommentDto(1L, "comment", "name", LocalDateTime.now());
        comment = new Comment(1L, "comment", item, user, LocalDateTime.now());
    }

    @Test
    void getItemByIdTest() {
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(null);
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L, 1L));

        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);

        Mockito
                .when(commentRepository.findCommentsById(1L))
                .thenReturn(new ArrayList<>());

        assertThat(itemService.getItemById(1L, 1L).getId().equals(1L));
    }


    @Test
    void findAllItemsTest() {
        Mockito
                .when(itemRepository.findAllByOwnerIdOrderByIdAsc(any()))
                .thenReturn(List.of(item));
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);

        assertThat(itemService.getAllByUserId(1L).size() == 1);
    }

    @Test
    void createItemTest() {

        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);
        Mockito
                .when(requestRepository.findRequestById(any()))
                .thenReturn(null);

        assertThat(itemService.add(itemDto, 1L).getOwnerId().equals(1L));
    }

    @Test
    void updateItemTest() {

        assertThrows(UserNotFoundException.class, () -> itemService.update(itemDto, 1L, 1L));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        assertThrows(NoSuchElementException.class, () -> itemService.update(itemDto, 1L, 1L));


        item.setOwnerId(11L);
        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);

        assertThrows(NoRightsToUpdateException.class, () -> itemService.update(itemDto, 1L, 1L));

        item.setOwnerId(user.getId());

        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);

        assertThat(itemRepository.save(item).getId().equals(1L));

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(any());

        itemDto.setName(null);
        itemDto.setDescription(null);
        itemDto.setAvailable(null);
        assertThrows(NoSuchElementException.class, () -> itemService.update(itemDto, 1L, 1L));

        itemDto.setName("null");
        itemDto.setDescription("null");
        itemDto.setAvailable(true);
        assertThat(itemService.update(itemDto, 1L, 1L).getName().equals("null"));
        assertThat(itemService.update(itemDto, 1L, 1L).getDescription().equals("null"));
        assertThat(itemService.update(itemDto, 1L, 1L).getAvailable().equals(true));

    }

    @Test
    void searchItemTest() {

        assertThat(itemService.getItemsByText("").size() == 0);

        Mockito
                .when(itemRepository.searchItem(any()))
                .thenReturn(List.of(item));

        assertThat(itemService.getItemsByText("text").size() == 1);
    }

    @Test
    void addCommentTest() {

        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(comment);
        Mockito
                .when(bookingRepository.findBookingsByItemAndBooker(any(), any()))
                .thenReturn(List.of(booking));

        assertThat(itemService.addComment(commentDto, 1L, 1L) != null);


        Mockito
                .when(bookingRepository.findBookingsByItemAndBooker(any(), any()))
                .thenReturn(List.of());

        assertThrows(CommentAddException.class, () -> itemService.addComment(commentDto, 1L, 1L));
    }

    @Test
    void deleteTest() {
        Mockito
                .when(itemRepository.findItemByOwnerId(any()))
                .thenReturn(item);
        assertThat(itemService.delete(itemDto, 1L).equals(item));
    }

    @Test
    void validatorTest() {


    }
}
