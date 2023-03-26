package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.enums.BookingStatus.APPROVED;

class ItemServiceTest {

    private ItemRepository mockItemRepository;
    private BookingRepository mockBookingRepository;
    private CommentRepository mockCommentRepository;
    private ItemRequestRepository mockItemRequestRepository;
    private UserRepository mockUserRepository;

    private UserService userService;
    private ItemService itemService;

    @BeforeEach
    void beforeEach() {
        mockItemRepository = Mockito.mock(ItemRepository.class);
        mockBookingRepository = Mockito.mock(BookingRepository.class);
        mockCommentRepository = Mockito.mock(CommentRepository.class);
        mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        mockUserRepository = Mockito.mock(UserRepository.class);

        userService = new UserService(mockUserRepository);
        itemService = new ItemService(mockItemRepository, mockBookingRepository, mockCommentRepository, mockItemRequestRepository, userService);
    }

    @Test
    void getByIdNormalWay() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        Item item = itemService.getById(1);

        assertEquals(simpleTestItem, item);
    }

    @Test
    void getByIdItemNotFound() {
        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.getById(1));

        Assertions.assertEquals("Вещь 1 не найдена", exception.getMessage());
    }

    private Item makeSimpleTestItem() {
        return Item.builder()
                .id(1)
                .ownerId(1)
                .name("name")
                .description("description")
                .available(true).build();
    }

    @Test
    void testGetByIdNormalWay() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User testUserOwner = new User(1, "name", "e@mail.ru");
        User testUserBooker = new User(1, "name", "e@mail.ru");
        Item simpleTestItem = makeSimpleTestItem();
        List<Comment> testCommentList = List.of(
                new Comment(1, "comment 1 text", simpleTestItem, testUserOwner, now.minusHours(1)),
                new Comment(2, "comment 2 text", simpleTestItem, testUserBooker, now.minusMinutes(1))
        );

        List<Booking> testBookingList = List.of(
                new Booking(1, simpleTestItem, testUserBooker, now.minusHours(2), now.minusHours(1), APPROVED),
                new Booking(2, simpleTestItem, testUserBooker, now.plusHours(1), now.plusHours(2), APPROVED)
        );

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        Mockito
                .when(mockCommentRepository.findByItem_idOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(testCommentList);

        Mockito
                .when(mockBookingRepository.findByItem_idIn(Mockito.any(List.class)))
                .thenReturn(testBookingList);

        Item item = itemService.getById(1, 1);

        assertEquals(1, item.getId());
        assertEquals(1, item.getOwnerId());
        assertEquals("name", item.getName());
        assertEquals("description", item.getDescription());
        assertEquals(testBookingList.get(0), item.getLastBooking());
        assertEquals(testBookingList.get(0), item.getLastBooking());
        assertEquals(testBookingList.get(1), item.getNextBooking());
        assertEquals(testCommentList.get(0), item.getComments().get(0));
        assertEquals(testCommentList.get(1), item.getComments().get(1));
    }

    @Test
    void createItemNormalWayWithoutRequest() {
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("name", "description", true, 0);
        Item testItem = ItemDtoMapper.toItem(itemCreateRequest);
        testItem.setOwnerId(1);

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(testItem);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1, "name", "e@mail.ru")));

        Item item = itemService.create(itemCreateRequest, 1);

        assertEquals(testItem, item);
    }

    @Test
    void createItemNormalWayWithRequest() {
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("name", "description", true, 1);
        User testOwnerUser = new User(1, "name", "e@mail.ru");
        User testRequestUser = new User(2, "name2", "e@mail2.ru");

        ItemRequest testItemRequest = ItemRequest.builder()
                .requestId(1)
                .requestAuthor(testRequestUser)
                .description("хочу вещь name")
                .created(LocalDateTime.now()).build();

        Item testItem = ItemDtoMapper.toItem(itemCreateRequest);
        testItem.setOwnerId(1);
        testItem.setItemRequest(testItemRequest);

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(testItem);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testOwnerUser));

        Mockito
                .when(mockItemRequestRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(testItemRequest);

        Item item = itemService.create(itemCreateRequest, 1);

        assertEquals(testItem, item);
    }

    @Test
    void createItemUserNotFound() {
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("name", "description", true, 0);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.create(itemCreateRequest, 1));

        Assertions.assertEquals("Пользователь 1 не найден", exception.getMessage());
    }

    @Test
    void updateItemNormalWay() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        Mockito
                .when(mockItemRepository.save(Mockito.any(Item.class)))
                .thenReturn(simpleTestItem);

        Item item = itemService.update(simpleTestItem, 1);

        assertEquals(simpleTestItem, item);
    }

    @Test
    void updateItemNotFound() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.update(simpleTestItem, 1));

        Assertions.assertEquals("Вещь 1 не найдена", exception.getMessage());
    }

    @Test
    void updateItemOwnedByAnotherUser() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        final ItemAccessDeniedException exception = Assertions.assertThrows(
                ItemAccessDeniedException.class,
                () -> itemService.update(simpleTestItem, 2));

        Assertions.assertEquals("Вещь принадлежит другому пользователю", exception.getMessage());
    }

    @Test
    void deleteItemNormalWay() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        itemService.delete(1, 1);

        Mockito.verify(mockItemRepository, Mockito.times(1))
                .delete(simpleTestItem);
    }

    @Test
    void deleteItemNotFound() {
        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.delete(1, 1));

        Assertions.assertEquals("Вещь 1 не найдена", exception.getMessage());
    }

    @Test
    void deleteItemOwnedByAnotherUser() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        final ItemAccessDeniedException exception = Assertions.assertThrows(
                ItemAccessDeniedException.class,
                () -> itemService.delete(1, 2));

        Assertions.assertEquals("Вещь принадлежит другому пользователю", exception.getMessage());
    }

    @Test
    void searchItemsNormalWay() {
        Item simpleTestItem = makeSimpleTestItem();

        Mockito
                .when(
                        mockItemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.anyBoolean(),
                                Mockito.any(Pageable.class)))
                .thenReturn(List.of(simpleTestItem));

        List<Item> foundItems = itemService.search("name", 0, 20);

        assertEquals(1, foundItems.size());
    }

    @Test
    void searchItemsNormalWayEmptyText() {
        List<Item> foundItems = itemService.search("", 0, 20);

        assertEquals(0, foundItems.size());
    }

    @Test
    void searchItemsInvalidPaginationParamFrom() {
        final InvalidPaginationException exception = Assertions.assertThrows(
                InvalidPaginationException.class,
                () -> itemService.search("test", -1, 20));

        Assertions.assertEquals("Неверный параметр from", exception.getMessage());
    }

    @Test
    void searchItemsInvalidPaginationParamSize() {
        final InvalidPaginationException exception = Assertions.assertThrows(
                InvalidPaginationException.class,
                () -> itemService.search("test", 1, 0));

        Assertions.assertEquals("Неверный параметр size", exception.getMessage());
    }

    @Test
    void createCommentNormalWay() {
        Item simpleTestItem = makeSimpleTestItem();
        User testUser = new User(2, "name", "e@mail.ru");

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser));

        Mockito
                .when(
                        mockBookingRepository.findByItem_idAndBooker_idAndStatusAndRentEndDateIsBefore(
                                Mockito.anyLong(),
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().id(1).item(simpleTestItem).booker(testUser).build()));

        Mockito
                .when(mockCommentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(Comment.builder().id(1).text("comment text").author(testUser).item(simpleTestItem).build());


        Comment comment = itemService.createComment("comment text", 1, 2);

        assertEquals(1, comment.getId());
    }

    @Test
    void createCommentItemNotFound() {
        User testUser = new User(2, "name", "e@mail.ru");

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser));

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.createComment("test", 1, 2));

        Assertions.assertEquals("Вещь 1 не найдена", exception.getMessage());
    }

    @Test
    void createCommentUserNotFound() {
        Item simpleTestItem = makeSimpleTestItem();
        User testUser = new User(2, "name", "e@mail.ru");

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.createComment("test", 1, 2));

        Assertions.assertEquals("Пользователь 2 не найден", exception.getMessage());
    }

    @Test
    void createCommentByUserWithNoBookingsOfItem() {
        Item simpleTestItem = makeSimpleTestItem();
        User testUser = new User(2, "name", "e@mail.ru");

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(simpleTestItem));

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testUser));

        Mockito
                .when(
                        mockBookingRepository.findByItem_idAndBooker_idAndStatusAndRentEndDateIsBefore(
                                Mockito.anyLong(),
                                Mockito.anyLong(),
                                Mockito.any(BookingStatus.class),
                                Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of());

        final CommentAuthorHaveNoBookingsException exception = Assertions.assertThrows(
                CommentAuthorHaveNoBookingsException.class,
                () -> itemService.createComment("test", 1, 2));

        Assertions.assertEquals("Пользователь 2 не имеет завершенных бронирований вещи 1", exception.getMessage());
    }
}