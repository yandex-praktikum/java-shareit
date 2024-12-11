package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto addBooking(int userId, BookingSaveDto bookingSaveDto) {
        log.info("Запрос на добавление бронирования userId - {}, bookingSaveDto - {}", userId, bookingSaveDto);
        User user = getUserById(userId);
        int itemId = bookingSaveDto.getItemId();
        Item item = getItemById(itemId);
        if (!item.isAvailable()) {
            throw new NotValidException(Item.class, "Не доступно для бронирования");
        }
        Booking booking = bookingMapper.map(bookingSaveDto);
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new NotValidException(Booking.class,
                    "Дата начала бронирования не может быть позже конца бронирования.");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Сохраненное создание бронирования - {}", savedBooking);

        return bookingMapper.map(savedBooking);
    }

    @Override
    public BookingDto manageBooking(int userId, int bookingId, boolean approved) {
        log.info("Запрос на изменение бронирования по id - {}, Пользователем userId - {} и статусом - {}", bookingId, userId, approved);
        getUserById(userId);
        Booking booking = getBookingById(bookingId);
        log.info("Бронирование для изменения - {}", booking);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotValidException(Booking.class, "Только владелец может изменять бронирование");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Сохранение бронирования - {}", savedBooking);
        return bookingMapper.map(savedBooking);
    }

    @Override
    public BookingDto getBooking(int userId, int bookingId) {
        log.info("Запрос на получение бронирования по id - {}", bookingId);
        getUserById(userId);
        Booking booking = getBookingById(bookingId);
        log.info("Бронирование найдено - {}", booking);
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotValidException(Booking.class,
                    "Только владелец или человек оформивший бронирование может получить бронирование");
        }

        return bookingMapper.map(booking);
    }

    @Override
    public Collection<BookingDto> getAllUserBookings(int userId, BookingState state) {
        log.info("Запрос на получение всех бронирований по id - {}", userId);
        getUserById(userId);
        final Collection<Booking> bookings;
        final LocalDateTime current = LocalDateTime.now();
        switch (state) {
            case WAITING -> {
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            }
            case CURRENT -> {
                bookings = bookingRepository.findAllCurrentBookings(userId, current);
            }
            case PAST -> {
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, current);
            }
            case FUTURE -> {
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, current);
            }
            case ALL -> {
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            }
            default -> {
                throw new NotValidException(BookingState.class, "invalid");
            }
        }
        log.info("Все бронирования найдены - {}", bookings);
        return bookingMapper.map(bookings);
    }

    @Override
    public Collection<BookingDto> getAllUserItemsBookings(int userId, BookingState state) {
        log.info("Запрос на получение всех бронирований по userId - {}", userId);
        getUserById(userId);
        Collection<Integer> itemIds = itemRepository.findAllByOwnerId(userId).stream()
                .map(Item::getId)
                .toList();
        final Collection<Booking> bookings;
        final LocalDateTime current = LocalDateTime.now();
        switch (state) {
            case WAITING -> {
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds, BookingStatus.WAITING);
            }
            case REJECTED -> {
                bookings = bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds, BookingStatus.REJECTED);
            }
            case CURRENT -> {
                bookings = bookingRepository.findAllCurrentBookings(itemIds, current);
            }
            case PAST -> {
                bookings = bookingRepository.findAllByItemIdInAndEndBeforeOrderByStartDesc(itemIds, current);
            }
            case FUTURE -> {
                bookings = bookingRepository.findAllByItemIdInAndStartAfterOrderByStartDesc(itemIds, current);
            }
            case ALL -> {
                bookings = bookingRepository.findAllByItemIdInOrderByStartDesc(itemIds);
            }
            default -> {
                throw new NotValidException(BookingState.class, "invalid");
            }
        }
        log.info("Все бронирования userId - {}, найдены - {}", userId, bookings);
        return bookingMapper.map(bookings);
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ForbiddenException("Пользователь с ID " + userId + " не найден"));
    }

    private Item getItemById(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Предмет с ID " + itemId + " не найден"));
    }

    private Booking getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ItemNotFoundException("Бронирование  с ID " + bookingId + " не найдено"));
    }
}
