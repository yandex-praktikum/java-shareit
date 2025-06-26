package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.exception.ErrorRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto createBooking(Long userId, BookingDto bookingDto) {
        log.info("Пользователь с id {} создает бронь", userId);
        User user = getUser(userId);
        Item item = checkItem(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new ErrorRequestException("Вещь с id " + bookingDto.getItemId() + " недоступна для бронирования");
        }
        bookingDto.setStatus(String.valueOf(BookingStatus.WAITING));
        Booking bookingEntity = bookingMapper.mapBooking(bookingDto, user, item);
        return bookingMapper.mapBookingResponseDto(repository.save(bookingEntity));
    }

    @Override
    public BookingResponseDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Пользователь с id {} изменяет статус брони {}", userId, bookingId);
        Booking booking = checkBooking(bookingId);

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ErrorRequestException("Статус бронирования уже изменен");
        }

        Item item = checkItem(booking.getItem().getId());
        if (!item.getOwner().getId().equals(userId)) {
            throw new ErrorRequestException("Пользователь с id " + userId + " не может редактировать статус этой вещи");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.mapBookingResponseDto(repository.save(booking));
    }

    @Override
    public BookingResponseDto canceledBooking(Long userId, Long bookingId) {
        log.info("Пользователь с id {} отменяет бронь {}", userId, bookingId);
        Booking booking = checkBooking(bookingId);

        if (!booking.getBooker().getId().equals(userId)) {
            throw new ErrorRequestException("Пользователь с id " + userId + " не может отменить бронь, так как она не " +
                    "принадлежит ему");
        }

        booking.setStatus(BookingStatus.CANCELED);
        return bookingMapper.mapBookingResponseDto(repository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = checkBooking(bookingId);
        Item item = checkItem(booking.getItem().getId());

        if (!booking.getBooker().getId().equals(userId) && !item.getOwner().getId().equals(userId)) {
            throw new ErrorRequestException("Пользователь с id " + userId + " не может просматривать информации о " +
                    "бронировании");
        }

        return bookingMapper.mapBookingResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingByState(Long userId, String state) {
        BookingState bookingState = checkState(state);
        getUser(userId);
        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> bookings = switch (bookingState) {
            case WAITING -> repository.getBookingByStateStatus(userId, BookingStatus.WAITING);
            case REJECTED -> repository.getBookingByStateStatus(userId, BookingStatus.REJECTED);
            case CURRENT -> repository.getBookingByStateCurrent(userId, currentTime);
            case PAST -> repository.getBookingByStatePast(userId, currentTime);
            case FUTURE -> repository.getBookingByStateFuture(userId, currentTime);
            default -> repository.getBookingByStateALL(userId);
        };

        return bookingMapper.mapListBookingResponseDto(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsAllItemsByState(String state, Long userId) {
        BookingState bookingState = checkState(state);
        getUser(userId);
        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> bookings = switch (bookingState) {
            case WAITING -> repository.getBookingAllItemsByStateStatus(userId, BookingStatus.WAITING);
            case REJECTED -> repository.getBookingAllItemsByStateStatus(userId, BookingStatus.REJECTED);
            case CURRENT -> repository.getBookingAllItemsByStateCurrent(userId, currentTime);
            case PAST -> repository.getBookingAllItemsByStatePast(userId, currentTime);
            case FUTURE -> repository.getBookingAllItemsByStateFuture(userId, currentTime);
            default -> repository.getBookingAllItemsByStateALL(userId);
        };

        return bookingMapper.mapListBookingResponseDto(bookings);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя с id:" + userId));
    }

    private Item checkItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти вещь с id:" + itemId));
    }

    private Booking checkBooking(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти бронь с id:" + bookingId));
    }

    private BookingState checkState(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ErrorRequestException("Не существует состояния " + state);
        }
    }
}
