package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User(1, "User1", "user1@mail.ru");
        User user2 = new User(2, "User2", "user2@mail.ru");
        User user3 = new User(3, "User3", "user3@mail.ru");
        ItemRequest itemRequest1 = new ItemRequest("description1", user2, LocalDateTime.now());
        ItemRequest itemRequest2 = new ItemRequest("description2", user1, LocalDateTime.now());
        ItemRequest itemRequest3 = new ItemRequest("description3", user3, LocalDateTime.now());
        Item item1 = new Item(1, "item1", "description1", true, user1, itemRequest1);
        Item item2 = new Item(2, "item2", "meow", true, user2, itemRequest2);
        Item item3 = new Item(3, "item3", "meow", true, user2, itemRequest3);
        Comment comment1 = new Comment(1, "comment1", item1, user1, LocalDateTime.of(2000, 2, 1, 10, 0));
        Comment comment2 = new Comment(2, "comment2", item1, user1, LocalDateTime.of(2000, 1, 30, 10, 0));
        Comment comment3 = new Comment(3, "comment3", item1, user1, LocalDateTime.of(2000, 1, 1, 10, 0));
        Booking booking1 = new Booking(1, LocalDateTime.of(2000, 5, 1, 10, 0), LocalDateTime.of(2000, 5, 2, 10, 0), item2, user1, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2000, 5, 3, 10, 0), LocalDateTime.of(2000, 5, 4, 10, 0), item2, user3, BookingStatus.WAITING);
        Booking booking3 = new Booking(3, LocalDateTime.of(2000, 5, 5, 10, 0), LocalDateTime.of(2000, 5, 6, 10, 0), item1, user2, BookingStatus.WAITING);
        Booking booking4 = new Booking(4, LocalDateTime.of(2000, 5, 10, 10, 0), LocalDateTime.of(2000, 5, 11, 10, 0), item1, user3, BookingStatus.APPROVED);
        Booking booking5 = new Booking(5, LocalDateTime.of(2000, 4, 10, 10, 0), LocalDateTime.of(2000, 5, 11, 10, 0), item3, user3, BookingStatus.APPROVED);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        itemRequestRepository.save(itemRequest3);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(3, page);

        Assertions.assertEquals(2, bookingList.size());
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAscTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(3, LocalDateTime.of(2000, 5, 10, 12, 0), LocalDateTime.of(2000, 5, 10, 12, 0), page);

        Assertions.assertEquals(2, bookingList.size());
        Assertions.assertEquals("item1", bookingList.get(0).getItem().getName());
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(3, LocalDateTime.of(2000, 5, 5, 12, 0), page);

        Assertions.assertEquals(1, bookingList.size());
        Assertions.assertEquals("item2", bookingList.get(0).getItem().getName());
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(3, LocalDateTime.of(2000, 5, 5, 12, 0), page);

        Assertions.assertEquals(1, bookingList.size());
        Assertions.assertEquals("item1", bookingList.get(0).getItem().getName());
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(3, BookingStatus.APPROVED, page);

        Assertions.assertEquals(2, bookingList.size());
        Assertions.assertEquals("item1", bookingList.get(0).getItem().getName());
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(2, page);

        Assertions.assertEquals(2, bookingList.size());
        Assertions.assertEquals("item2", bookingList.get(0).getItem().getName());
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(2, LocalDateTime.of(2000, 5, 3, 12, 0), LocalDateTime.of(2000, 5, 3, 12, 0), page);

        Assertions.assertEquals(2, bookingList.size());
        Assertions.assertEquals("item2", bookingList.get(0).getItem().getName());
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 2);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(2, LocalDateTime.of(2000, 6, 3, 12, 0),  page);

        Assertions.assertEquals(2, bookingList.size());
        Assertions.assertEquals("item2", bookingList.get(0).getItem().getName());
        Assertions.assertEquals("item2", bookingList.get(1).getItem().getName());
    }

    @Test
    void findAllByItemOwnerIdAndStartAfterOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 3);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(2, LocalDateTime.of(2000, 3, 3, 12, 0),  page);

        Assertions.assertEquals(3, bookingList.size());
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDescTest() {
        PageRequest page = PageRequest.of(0, 3);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(2, BookingStatus.APPROVED, page);

        Assertions.assertEquals(1, bookingList.size());
    }

    @Test
    void findAllByItemIdAndStatusNotOrderByStartAscTest() {
        List<Booking> bookingList = bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(3, BookingStatus.WAITING);

        Assertions.assertEquals(1, bookingList.size());
        Assertions.assertEquals(5, bookingList.get(0).getId());
    }

    @Test
    void findAllByItemIdAndBookerIdAndEndBeforeTest() {
        List<Booking> bookingList = bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(2, 3, LocalDateTime.of(2000, 5, 5, 12, 0));

        Assertions.assertEquals(1, bookingList.size());
        Assertions.assertEquals(2, bookingList.get(0).getId());
    }
}

