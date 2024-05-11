package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;
import ru.practicum.shareit.item.exception.IncorrectUserException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.PaginationException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void addItem_withRequest() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        Item item = new Item(1, "item", "description", true, owner, itemRequest);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(userRepository.findById(2)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(itemRequest));

        itemService.addItem(itemDto, 2);

        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void addItem_withoutRequest() {
        User owner = new User(2, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(userRepository.findById(2)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any())).thenReturn(item);

        itemService.addItem(itemDto, 2);

        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void updateItem_ThrowsIncorrectUserException() {
        User owner = new User(2, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(IncorrectUserException.class, () -> itemService.updateItem(itemDto, 1, 1));
    }

    @Test
    void updateItem_correctWork() {
        User owner = new User(2, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        itemService.updateItem(itemDto, 1, 2);

        verify(itemRepository, times(1)).findById(1);
    }

    @Test
    void getItemById_ByOwner() {
        User user = new User(1, "user", "user@mail.ru");
        User user2 = new User(2, "user2", "user2@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2000, 1, 10, 10, 0),
                LocalDateTime.of(2000, 1, 10, 15, 0), item, user, BookingStatus.APPROVED);
        Booking booking2 = new Booking(2, LocalDateTime.of(2025, 1, 10, 15, 0),
                LocalDateTime.of(2000, 1, 10, 20, 0), item, user2, BookingStatus.APPROVED);
        List<Booking> bookings = List.of(booking1, booking2);
        Comment comment1 = new Comment(1, "comment1", item, user, LocalDateTime.of(2000, 2, 10, 10, 0));
        List<Comment> commentList = List.of(comment1);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(1, BookingStatus.REJECTED)).thenReturn(bookings);
        when(commentRepository.findAllByItemIdOrderByCreatedAsc(1)).thenReturn(commentList);

        ItemDtoBookingsAndComments itemDtoBookingsAndComments = itemService.getItemById(3, 1);

        Assertions.assertEquals("item",itemDtoBookingsAndComments.getName());
        Assertions.assertEquals(1, itemDtoBookingsAndComments.getComments().size());
    }

    @Test
    void getItemById_WithoutBookings() {
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(1, BookingStatus.REJECTED)).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByItemIdOrderByCreatedAsc(1)).thenReturn(new ArrayList<>());

        ItemDtoBookingsAndComments itemDtoBookingsAndComments = itemService.getItemById(3, 1);

        Assertions.assertEquals("item",itemDtoBookingsAndComments.getName());
    }

    @Test
    void getItemById_ByNotOwner() {
        User user = new User(1, "user", "user@mail.ru");
        User user2 = new User(2, "user2", "user2@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2000, 1, 10, 10, 0),
                LocalDateTime.of(2000, 1, 10, 15, 0), item, user, BookingStatus.APPROVED);
        Booking booking2 = new Booking(2, LocalDateTime.of(2025, 1, 10, 15, 0),
                LocalDateTime.of(2000, 1, 10, 20, 0), item, user2, BookingStatus.APPROVED);
        List<Booking> bookings = List.of(booking1, booking2);
        Comment comment1 = new Comment(1, "comment1", item, user, LocalDateTime.of(2000, 2, 10, 10, 0));
        List<Comment> commentList = List.of(comment1);
        List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(commentList);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(1, BookingStatus.REJECTED)).thenReturn(bookings);
        when(commentRepository.findAllByItemIdOrderByCreatedAsc(1)).thenReturn(commentList);

        ItemDtoBookingsAndComments itemDtoBookingsAndComments = itemService.getItemById(2, 1);

        Assertions.assertEquals("item",itemDtoBookingsAndComments.getName());
    }

    @Test
    void getUserItems_PaginationException() {
        Assertions.assertThrows(PaginationException.class, () -> itemService.getUserItems(1, -1, 2));
    }

    @Test
    void getUserItems_UserItemsEmpty() {
        PageRequest page = PageRequest.of(0, 2);
        when(itemRepository.findByOwnerId(1, page)).thenReturn(new ArrayList<>());

        List<ItemDtoBookingsAndComments> itemDtoBookingsAndComments = itemService.getUserItems(1, 0, 2);

        Assertions.assertEquals(0, itemDtoBookingsAndComments.size());
    }

    @Test
    void getUserItems_UserItemsNotEmptyWithBookings() {
        User user = new User(1, "user", "user@mail.ru");
        User user2 = new User(2, "user2", "user2@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2000, 1, 10, 10, 0),
                LocalDateTime.of(2000, 1, 10, 15, 0), item, user, BookingStatus.APPROVED);
        Booking booking2 = new Booking(2, LocalDateTime.of(2025, 1, 10, 15, 0),
                LocalDateTime.of(2000, 1, 10, 20, 0), item, user2, BookingStatus.APPROVED);
        List<Booking> bookings = List.of(booking1, booking2);
        Comment comment1 = new Comment(1, "comment1", item, user, LocalDateTime.of(2000, 2, 10, 10, 0));
        List<Comment> commentList = List.of(comment1);

        PageRequest page = PageRequest.of(0, 2);

        when(itemRepository.findByOwnerId(3, page)).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(1, BookingStatus.REJECTED)).thenReturn(bookings);
        when(commentRepository.findAllByItemIdOrderByCreatedAsc(1)).thenReturn(commentList);

        List<ItemDtoBookingsAndComments> itemDtoBookingsAndComments = itemService.getUserItems(3, 0, 2);

        Assertions.assertEquals(1, itemDtoBookingsAndComments.size());
    }

    @Test
    void getUserItems_UserItemsNotEmptyWithoutBookings() {
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);

        PageRequest page = PageRequest.of(0, 2);

        when(itemRepository.findByOwnerId(3, page)).thenReturn(List.of(item));

        List<ItemDtoBookingsAndComments> itemDtoBookingsAndComments = itemService.getUserItems(3, 0, 2);

        Assertions.assertEquals(1, itemDtoBookingsAndComments.size());
    }

    @Test
    void getSearch_PaginationException() {
        Assertions.assertThrows(PaginationException.class, () -> itemService.getSearch("text", 0, -2));
    }

    @Test
    void getSearch_TextIsEmpty() {
        List<ItemDto> itemDtos = itemService.getSearch("", 0, 2);
        Assertions.assertEquals(0, itemDtos.size());
    }

    @Test
    void getSearch_NotEmpty() {
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item1 = new Item(1, "item1", "description", true, owner);
        Item item2 = new Item(1, "item2", "meow", true, owner);
        PageRequest page = PageRequest.of(0, 2);

        when(itemRepository.findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue("meow", "meow", page)).thenReturn(List.of(item2));

        List<ItemDto> itemDtos = itemService.getSearch("meow", 0, 2);

        Assertions.assertEquals(1, itemDtos.size());
        verify(itemRepository, times(1)).findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue("meow", "meow", page);
    }

    @Test
    void addComment_ValidationExceptionNoText() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Comment comment1 = new Comment(1, "", item, user, LocalDateTime.of(2000, 2, 10, 10, 0));
        CommentDto commentDto = CommentMapper.toCommentDto(comment1);

        Assertions.assertThrows(ValidationException.class, () -> itemService.addComment(commentDto,1,1));
    }

    @Test
    void addComment_ValidationExceptionNotBooked() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Comment comment1 = new Comment(1, "comment", item, user, LocalDateTime.of(2000, 2, 10, 10, 0));
        CommentDto commentDto = CommentMapper.toCommentDto(comment1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(ValidationException.class, () -> itemService.addComment(commentDto,1,1));
    }

    @Test
    void addComment_Correct() {
        User user = new User(1, "user", "user@mail.ru");
        User user2 = new User(2, "user2", "user2@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2000, 1, 10, 10, 0),
                LocalDateTime.of(2000, 1, 10, 15, 0), item, user, BookingStatus.APPROVED);
        Booking booking2 = new Booking(2, LocalDateTime.of(2025, 1, 10, 15, 0),
                LocalDateTime.of(2000, 1, 10, 20, 0), item, user2, BookingStatus.APPROVED);
        List<Booking> bookings = List.of(booking1);
        Comment comment1 = new Comment(1, "comment1", item, user, LocalDateTime.of(2000, 1, 10, 15, 0));
        List<Comment> commentList = List.of(comment1);
        CommentDto commentDto = CommentMapper.toCommentDto(comment1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(eq(1), eq(1), any())).thenReturn(bookings);
        when(commentRepository.save(any())).thenReturn(comment1);

        CommentDto result = itemService.addComment(commentDto, 1, 1);
        Assertions.assertEquals("comment1", result.getText());
    }
}
