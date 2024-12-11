package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookingServiceImplTest {
    private final EntityManager entityManager;
    private final BookingService bookingService;
    private final BookingMapper mapper;

    private Booking bookingExpected;
    private Item itemExpected;
    private User userExpected;

    @BeforeEach
    public void setUp() {

        userExpected = new User();
        userExpected.setId(30);
        userExpected.setName("user3");
        userExpected.setEmail("user3@somemail.ru");

        itemExpected = new Item();
        itemExpected.setId(30);
        itemExpected.setName("item3");
        itemExpected.setDescription("description3");
        itemExpected.setAvailable(true);
        itemExpected.setOwner(userExpected);

        bookingExpected = new Booking();
        bookingExpected.setId(1);
        bookingExpected.setItem(itemExpected);
        bookingExpected.setBooker(userExpected);
        bookingExpected.setStatus(BookingStatus.WAITING);
        bookingExpected.setStart(LocalDateTime.of(2024, 12, 11, 11, 11, 11));
        bookingExpected.setEnd(LocalDateTime.of(2024, 12, 11, 11, 12, 11));


    }


    @Test
    public void addBookingTest() {
        int userId = userExpected.getId();
        BookingSaveDto bookingSaveDto = new BookingSaveDto();
        bookingSaveDto.setItemId(itemExpected.getId());
        bookingSaveDto.setStart(bookingExpected.getStart());
        bookingSaveDto.setEnd(bookingExpected.getEnd());

        bookingService.addBooking(userId, bookingSaveDto);
        TypedQuery<Booking> query =
                entityManager.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingExpected.getId()).getSingleResult();
        BookingDto bookingDto = mapper.map(booking);

        BookingDto bookingDtoExpected = mapper.map(bookingExpected);
        assertThat(bookingDto, allOf(
                hasProperty("id",
                        equalTo(bookingDtoExpected.getId())),
                hasProperty("start",
                        equalTo(bookingDtoExpected.getStart())),
                hasProperty("end",
                        equalTo(bookingDtoExpected.getEnd())),
                hasProperty("item",
                        allOf(hasProperty("id", equalTo(bookingDtoExpected.getItem().getId())))),
                hasProperty("booker",
                        allOf(hasProperty("id", equalTo(bookingDtoExpected.getBooker().getId())))),
                hasProperty("status",
                        equalTo(bookingDtoExpected.getStatus()))
        ));
    }

    @Test
    void manageBookingTest() {
        int userId = 10;
        int bookingId = 10;
        boolean approved = true;

        bookingService.manageBooking(userId, bookingId, approved);
        TypedQuery<Booking> query = entityManager.createQuery("SELECT b FROM Booking AS b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingId).getSingleResult();
        BookingDto bookingDto = mapper.map(booking);

        long bookerExpectedId = 20;
        assertThat(bookingDto, allOf(
                hasProperty("id", equalTo(bookingId)),
                hasProperty("booker", allOf(hasProperty("id", equalTo(bookerExpectedId)))),
                hasProperty("status", equalTo(BookingStatus.APPROVED))
        ));
    }

    @Test
    void getBookingTest() {
        int userId = 20;
        int bookingId = 10;

        BookingDto bookingDto = bookingService.getBooking(userId, bookingId);

        BookingDto bookingId10DtoExpected = new BookingDto();
        bookingId10DtoExpected.setId(10);
        LocalDateTime start = LocalDateTime.of(2024, 11, 21, 10, 10, 10);
        bookingId10DtoExpected.setStart(start);
        LocalDateTime end = LocalDateTime.of(2024, 11, 21, 11, 11, 11);
        bookingId10DtoExpected.setEnd(end);
        ItemDto item = new ItemDto();
        item.setId(10);
        bookingId10DtoExpected.setItem(item);
        UserDto booker = new UserDto();
        booker.setId(20);
        bookingId10DtoExpected.setBooker(booker);
        bookingId10DtoExpected.setStatus(BookingStatus.APPROVED);

        assertThat(bookingDto, allOf(
                hasProperty("id",
                        equalTo(bookingId10DtoExpected.getId())),
                hasProperty("start",
                        equalTo(bookingId10DtoExpected.getStart())),
                hasProperty("end",
                        equalTo(bookingId10DtoExpected.getEnd())),
                hasProperty("item",
                        allOf(hasProperty("id", equalTo(bookingId10DtoExpected.getItem().getId())))),
                hasProperty("booker",
                        allOf(hasProperty("id", equalTo(bookingId10DtoExpected.getBooker().getId())))),
                hasProperty("status",
                        equalTo(bookingId10DtoExpected.getStatus()))
        ));
    }

    @Test
    void getAllUserBookingsTest() {
        int userId = 20;
        BookingState state = BookingState.WAITING;

        Collection<BookingDto> bookings = bookingService.getAllUserItemsBookings(userId, state);

        bookings.forEach(booking -> assertThat(booking, allOf(
                hasProperty("id", notNullValue()),
                hasProperty("start", nullValue()),
                hasProperty("end", nullValue()),
                hasProperty("item", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("name", notNullValue()),
                        hasProperty("description", notNullValue()),
                        hasProperty("available", notNullValue())
                )),
                hasProperty("booker", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("email", notNullValue()),
                        hasProperty("name", notNullValue())
                )),
                hasProperty("status", notNullValue()))
        ));
    }

    @Test
    void getAllUserItemsBookingsTest() {
        int userId = 20;
        BookingState state = BookingState.ALL;

        Collection<BookingDto> bookings = bookingService.getAllUserItemsBookings(userId, state);
        bookings.forEach(booking -> assertThat(booking, allOf(
                hasProperty("id", notNullValue()),
                hasProperty("start", nullValue()),
                hasProperty("end", nullValue()),
                hasProperty("item", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("name", notNullValue()),
                        hasProperty("description", notNullValue()),
                        hasProperty("available", notNullValue())
                )),
                hasProperty("booker", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("email", notNullValue()),
                        hasProperty("name", notNullValue())
                )),
                hasProperty("status", notNullValue()))
        ));
    }
}