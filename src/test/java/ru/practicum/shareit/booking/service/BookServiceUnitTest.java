package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
    Date dateBeg = getDate("2023-03-29");
    Date dateEnd = getDate("2023-04-15");

    User owner = new User(1, "eee@email.ru", "Eva");

    Item item = new Item(1, "carpet", "description", true, null, owner);

    ItemDto itemDto = ItemMapper.toItemDto(item);

    BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
    UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final BookingServiceImpl bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository, mockItemRepository);

    @Test
    public void shouldReturnExceptionUserNotFound() {
        Mockito.when(mockItemRepository.findById(1)).thenReturn(Optional.of(item));

        BookingDto bookingDto = new BookingDto(1, dateBeg, dateEnd, 1, itemDto, null, 99, null);

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.booking(99, bookingDto));

        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    public void shouldReturnIncorrectParameterException() {
        Mockito.when(mockItemRepository.findById(1)).thenReturn(Optional.of(item));

        BookingDto bookingDto = new BookingDto(1, dateBeg, dateEnd, 1, itemDto, null, 1, null);

        IncorrectParameterException exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> bookingService.booking(1, bookingDto));

        Assertions.assertEquals("Неверные параметры", exception.getParameter());
    }

    @Test
    public void shouldReturnBookingNotFoundException() {
        Mockito.when(mockBookingRepository.findById(any())).thenReturn(Optional.empty());

        BookingNotFoundException exception = Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBooking(1, 1));

        Assertions.assertEquals("Брони с таким id нет", exception.getMessage());
    }

    @Test
    public void shouldSuccessBook() {
        Mockito.when(mockItemRepository.findById(1)).thenReturn(Optional.of(item));
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(2)).thenReturn(Optional.of(booker));

        BookingDto bookingDto = new BookingDto(1, dateBeg, dateEnd, 1, itemDto, null, 2, null);

        BookingDto newBooking = bookingService.booking(2, bookingDto);

        Assertions.assertNotNull(newBooking);
    }

    @Test
    public void shouldSuccessApprove() {
        Mockito.when(mockItemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Booking booking = new Booking(1, dateBeg, dateEnd, item, booker, "WAITING");
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));

        BookingDto newBookingApprove = bookingService.aprove(owner.getId(), 1, true);

        Assertions.assertNotNull(newBookingApprove);
        Assertions.assertEquals(newBookingApprove.getStatus(), "APPROVED");
    }

    @Test
    public void shouldSuccessReject() {
        Mockito.when(mockItemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Booking booking = new Booking(1, dateBeg, dateEnd, item, booker, "WAITING");
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));

        BookingDto newBookingReject = bookingService.aprove(owner.getId(), 2, false);

        Assertions.assertNotNull(newBookingReject);
        Assertions.assertEquals(newBookingReject.getStatus(), "REJECTED");
    }

    @Test
    public void shouldFailedApproveOnWrongOwner() {
        Mockito.when(mockItemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Booking booking = new Booking(1, dateBeg, dateEnd, item, booker, "WAITING");
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));

        BookingNotFoundException exception = Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.aprove(2, 1, true));

        Assertions.assertEquals(exception.getMessage(), "Неверные параметры");
    }

    @Test
    public void shouldFailedApproveOnNotFoundBooking() {
        Mockito.when(mockBookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        BookingNotFoundException exception = Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.aprove(2, 1, true));

        Assertions.assertEquals(exception.getMessage(), "Брони с такой ID нет");
    }

    @Test
    public void shouldSuccessGetBooking() {
        Mockito.when(mockItemRepository.findById(1)).thenReturn(Optional.of(item));
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(2)).thenReturn(Optional.of(booker));

        Booking booking = new Booking(1, dateBeg, dateEnd, item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findById(1)).thenReturn(Optional.of(booking));

        BookingDto newBooking = bookingService.getBooking(2, 1);

        Assertions.assertNotNull(newBooking);
    }

    @Test
    public void shouldGetBookingWithFailedUser() {
        Mockito.when(mockItemRepository.findById(1)).thenReturn(Optional.of(item));
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(2)).thenReturn(Optional.of(booker));

        Booking booking = new Booking(1, dateBeg, dateEnd, item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findById(1)).thenReturn(Optional.of(booking));

        BookingNotFoundException exception = Assertions.assertThrows(BookingNotFoundException.class,
                () -> bookingService.getBooking(3, 1));

        Assertions.assertEquals("Неверные параметры", exception.getMessage());
    }

    @Test
    public void shouldSuccessGetApprovedCurrentBooking() {
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Booking booking1 = new Booking(1, getDate("2023-03-15"), getDate("2023-03-30"), item, booker, "APPROVED");
        Booking booking2 = new Booking(1, getDate("2023-03-01"), getDate("2023-03-30"), item, booker, "REJECTED");
        Booking booking3 = new Booking(1, getDate("2023-02-14"), getDate("2023-03-30"), item, booker, "APPROVED");
        Booking booking4 = new Booking(1, getDate("2023-02-01"), getDate("2023-02-20"), item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findByBookerByPage(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(List.of(booking1, booking2, booking3, booking4));

        List<BookingDto> newBooking = bookingService.getBooking(BookingStatus.CURRENT, 2, 1, 4);

        Assertions.assertTrue(newBooking.get(0).getStart().before(new Date()));
        Assertions.assertTrue(newBooking.get(0).getEnd().after(new Date()));
        Assertions.assertEquals(newBooking.size(), 3);
    }

    @Test
    public void shouldSuccessGetPastBooking() {
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Booking booking1 = new Booking(1, getDate("2023-03-15"), getDate("2023-03-30"), item, booker, "APPROVED");
        Booking booking2 = new Booking(1, getDate("2023-03-01"), getDate("2023-03-30"), item, booker, "REJECTED");
        Booking booking3 = new Booking(1, getDate("2023-02-14"), getDate("2023-03-30"), item, booker, "APPROVED");
        Booking booking4 = new Booking(1, getDate("2023-02-01"), getDate("2023-02-20"), item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findByBookerByPage(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(List.of(booking1, booking2, booking3, booking4));

        List<BookingDto> newBooking = bookingService.getBooking(BookingStatus.PAST, 2, 1, 4);

        Assertions.assertNotNull(newBooking);
        Assertions.assertTrue(newBooking.get(0).getStart().before(new Date()));
        Assertions.assertTrue(newBooking.get(0).getEnd().before(new Date()));
    }

    @Test
    public void shouldGetByBookingStatus() {
        User booker = new User(2, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booker));

        Booking booking1 = new Booking(1, getDate("2023-05-15"), getDate("2023-08-30"), item, booker, "APPROVED");
        Booking booking2 = new Booking(1, getDate("2023-03-01"), getDate("2023-03-30"), item, booker, "REJECTED");
        Booking booking3 = new Booking(1, getDate("2023-02-14"), getDate("2023-03-30"), item, booker, "APPROVED");
        Booking booking4 = new Booking(1, getDate("2023-02-01"), getDate("2023-02-20"), item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findByBookerByPage(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(List.of(booking1, booking2, booking3, booking4));

        List<BookingDto> newFutureBooking = bookingService.getBooking(BookingStatus.FUTURE, 2, 1, 4);

        Assertions.assertNotNull(newFutureBooking);
        Assertions.assertTrue(newFutureBooking.get(0).getStart().after(new Date()));
        Assertions.assertTrue(newFutureBooking.get(0).getEnd().after(new Date()));

        List<BookingDto> newRejectedBooking = bookingService.getBooking(BookingStatus.REJECTED, 2, 1, 4);

        Assertions.assertEquals(newRejectedBooking.size(), 1);


        List<BookingDto> newAllBooking = bookingService.getBooking(BookingStatus.ALL, 2, 1, 4);

        Assertions.assertEquals(newAllBooking.size(), 4);
    }

    @Test
    public void shouldSuccessGetOwnerItemsBookingsLists() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(owner));
        Mockito.when(mockItemRepository.findByOwner(any())).thenReturn(List.of(item));

        User booker = new User(2, "sss@email.ru", "Sasha");
        Booking booking2 = new Booking(1, getDate("2023-03-01"), getDate("2023-03-30"), item, booker, BookingStatus.WAITING.label);
        Booking booking1 = new Booking(1, getDate("2023-03-15"), getDate("2023-03-30"), item, booker, BookingStatus.WAITING.label);
        Booking booking3 = new Booking(1, getDate("2023-02-14"), getDate("2023-03-30"), item, booker, BookingStatus.REJECTED.label);
        Booking booking4 = new Booking(1, getDate("2023-02-01"), getDate("2023-02-20"), item, booker, BookingStatus.CURRENT.label);
        Mockito.when(mockBookingRepository.findByItem(any())).thenReturn(List.of(booking1, booking2, booking3, booking4));
        List<BookingDto> ownerItemsBookingLists = bookingService.ownerItemsBookingLists(BookingStatus.WAITING, 2, null, null);


        Assertions.assertTrue(ownerItemsBookingLists.get(0).getStatus().equals(BookingStatus.WAITING.label));
        Assertions.assertEquals(ownerItemsBookingLists.size(), 2);
    }

    @Test
    public void shouldSuccessGetOwnerItemsBookingsListsByPageParam() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(owner));
        Mockito.when(mockItemRepository.findByOwner(any())).thenReturn(List.of(item));

        User booker = new User(2, "sss@email.ru", "Sasha");
        Booking booking2 = new Booking(1, getDate("2023-03-01"), getDate("2023-03-30"), item, booker, BookingStatus.WAITING.label);
        Booking booking1 = new Booking(1, getDate("2023-03-15"), getDate("2023-03-30"), item, booker, BookingStatus.WAITING.label);
        Booking booking3 = new Booking(1, getDate("2023-02-14"), getDate("2023-03-30"), item, booker, BookingStatus.REJECTED.label);
        Booking booking4 = new Booking(1, getDate("2023-02-01"), getDate("2023-02-20"), item, booker, BookingStatus.CURRENT.label);
        Mockito.when(mockBookingRepository.findByItemByLimits(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(List.of(booking1, booking2, booking3, booking4));

        List<BookingDto> ownerItemsBookingLists = bookingService.ownerItemsBookingLists(BookingStatus.WAITING, 2, 1, 4);

        Assertions.assertTrue(ownerItemsBookingLists.get(0).getStatus().equals(BookingStatus.WAITING.label));
        Assertions.assertEquals(ownerItemsBookingLists.size(), 2);
    }

    @Test
    public void shouldSuccessGetOwnerItemsBookingsListsWithNotExistsUser() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.ownerItemsBookingLists(BookingStatus.WAITING, 2, 1, 4));

        Assertions.assertEquals(exception.getMessage(), "Пользователь не найден");
    }

    @Test
    public void shouldMapToBooking() {
        BookingDto bookingDto = new BookingDto(1, dateBeg, dateEnd, 1, itemDto, null, 99, null);
        Booking booking = BookingMapper.toBooking(bookingDto);

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(booking.getDateBegin(), bookingDto.getStart());
        Assertions.assertEquals(booking.getDateEnd(), bookingDto.getEnd());
    }

    private Date getDate(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
