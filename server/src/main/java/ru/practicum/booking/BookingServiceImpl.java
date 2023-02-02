package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.utilities.DateUtils;
import ru.practicum.utilities.PaginationConverter;
import ru.practicum.exception.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PaginationConverter paginationConverter;

    @Override
    public BookingDtoResponse getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking != null) {
            log.info("booking with id " + bookingId + " returned");
            return BookingMapper.toDtoResponse(booking);
        } else {
            log.error("non-existent booking with id " + bookingId);
            throw new BookingNotFoundException("can't find booking by id " + bookingId);
        }
    }

    @Override
    public List<BookingDtoResponse> getByUserId(Long userId, String state, Integer from, Integer size) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("unknown user with id " + userId);
        }
        List<Booking> books;
        Pageable pageable = paginationConverter.convert(from, size, "start");
        switch (state.toUpperCase()) {
            case "ALL":
                log.info("all bookings returned by user id " + userId);
                books = bookingRepository.findBookingsByBooker(userId, pageable);
                return addBookingsToDtoList(books);
            case "FUTURE":
                log.info("future bookings returned by user id " + userId);
                books = bookingRepository.findBookingsByBookerFuture(userId, pageable);
                return addBookingsToDtoList(books);
            case "CURRENT":
                books = bookingRepository.findBookingsByBookerCurrent(userId, pageable);
                List<BookingDtoResponse> dtoBooks = addBookingsToDtoList(books);
                for (BookingDtoResponse book : dtoBooks) {
                    book.setStart(book.getStart().minusNanos(1));
                    book.setEnd(book.getEnd().minusNanos(1));
                }
                log.info("current bookings returned by user id " + userId);
                return dtoBooks;
            case "PAST":
                log.info("past bookings returned by user id " + userId);
                books = bookingRepository.findBookingsByBookerPast(userId, pageable);
                return addBookingsToDtoList(books);
            case "WAITING":
            case "REJECTED":
                log.info("rejected bookings returned by user id " + userId);
                books = bookingRepository
                        .findBookingsByBookerWithState(userId, Status.valueOf(state), pageable);
                return addBookingsToDtoList(books);
            default:
                throw new UnknownStatusException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDtoResponse> getByOwnerId(Long ownerId, String state, Integer from, Integer size) {
        User user = userRepository.getUserById(ownerId);
        if (user == null) {
            throw new UserNotFoundException("unknown owner with id " + ownerId);
        }
        Pageable pageable = paginationConverter.convert(from, size, "start");
        switch (state) {
            case "ALL":
                log.info("all bookings returned by owner id " + ownerId);
                return addBookingsToDtoList(bookingRepository.findBookingsByOwner(ownerId, pageable));
            case "FUTURE":
                log.info("future bookings returned by owner id " + ownerId);
                return addBookingsToDtoList(bookingRepository.findBookingsByOwnerFuture(ownerId, pageable));
            case "CURRENT":
                List<BookingDtoResponse> books = addBookingsToDtoList(bookingRepository
                        .findBookingsByOwnerCurrent(ownerId, pageable));
                for (BookingDtoResponse book : books) {
                    book.setStart(book.getStart().minusNanos(1));
                    book.setEnd(book.getEnd().minusNanos(1));
                }
                log.info("current bookings returned by owner id " + ownerId);
                return books;
            case "PAST":
                log.info("past bookings returned by owner id " + ownerId);
                return addBookingsToDtoList(bookingRepository.findBookingsByOwnerPast(ownerId, pageable));
            case "WAITING":
            case "REJECTED":
                log.info("rejected bookings returned by owner id " + ownerId);
                return addBookingsToDtoList(bookingRepository
                        .findBookingsByOwnerWithState(ownerId, Status.valueOf(state), pageable));
            default:
                throw new UnknownStatusException("Unknown state: " + state);
        }
    }


    @Transactional
    @Override
    public BookingDtoResponse create(Long userId, BookingDto bookingDto) {
        Item item = itemRepository.findItemById(bookingDto.getItemId());
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("unknown user with id " + userId);
        }
        if (item == null) {
            throw new ItemNotFoundException("can't find item by id " + userId);
        }
        if (item.getOwnerId().equals(userId)) {
            throw new OwnerCreateBookingException("you can't book your items");
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("item is not available " + bookingDto.getItemId().toString());
        }

        if (!checkBookingDates(bookingDto)) {
            log.warn("incorrect dates in booking " + bookingDto);
            throw new ValidationException("dates must be correct");
        }
        Booking booking = BookingMapper.fromDto(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
        return BookingMapper.toDtoResponse(booking);
    }

    @Transactional
    @Override
    public BookingDtoResponse update(Long userId, Long bookingId, Boolean newStatus) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("unknown user with id " + userId);
        }
        Booking booking = bookingRepository.findBooking(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException("unknown booking with id " + bookingId);
        }
        Booking bookingWithOwner = bookingRepository.findBookingById(bookingId, userId);
        if (bookingWithOwner == null) {
            throw new BadUserForUpdateException("You don't have rights to update this booking " + booking);
        }
        Item item = itemRepository.findItemById(bookingWithOwner.getItem().getId());
        if (!item.getOwnerId().equals(userId)) {
            log.warn("user with id " + userId + " has no rights to update booking with id " + bookingId);
            throw new NoRightsToUpdateException("You don't have rights to update this booking");
        }
        if (bookingWithOwner.getStatus().equals(Status.APPROVED) && newStatus) {
            log.warn("duplicated approve to booking " + bookingId);
            throw new ValidationException("Booking " + bookingId + " already approved");
        }
        if (!newStatus) {
            bookingWithOwner.setStatus(Status.REJECTED);
        } else {
            bookingWithOwner.setStatus(Status.APPROVED);
        }
        log.info("booking " + bookingId + " updated");
        bookingWithOwner = bookingRepository.save(bookingWithOwner);
        return BookingMapper.toDtoResponse(bookingWithOwner);
    }

    private List<BookingDtoResponse> addBookingsToDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toDtoResponse)
                .collect(Collectors.toList());
    }

    private boolean checkBookingDates(BookingDto bookingDto) {
        return (bookingDto.getStart().isBefore(bookingDto.getEnd()) && bookingDto.getStart()
                .isAfter(DateUtils.now()));
    }
}
