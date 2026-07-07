package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public Booking findBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь при создании бронирования."));

        if (!booking.getItem().getOwner().getId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Бронирование не найдено");
        }

        return booking;
    }

    @Override
    public List<Booking> findAllBookingsByUser(Long userId, BookingState state) {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        return filterBookings(bookings, state);
    }

    public List<Booking> getOwnerBookings(Long ownerId, BookingState state) {
        List<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(ownerId);
        return filterBookings(bookings, state);
    }

    @Override
    public Booking createBooking(Booking booking, Long bookerId) {
        User booker = userService.getUserById(bookerId);
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена."));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Вещь недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("Владелец не может забронировать свою вещь");
        }

        if (!booking.getStart().isBefore(booking.getEnd())) {
            throw new IllegalArgumentException("Дата начала должна быть раньше даты окончания");
        }

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, Long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена."));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Статус может менять только владелец вещи.");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalArgumentException("Статус бронирования: " + booking.getStatus());
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    private List<Booking> filterBookings(List<Booking> bookings, BookingState state) {
        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case CURRENT -> bookings.stream()
                    .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                    .toList();
            case PAST -> bookings.stream()
                    .filter(b -> b.getEnd().isBefore(now))
                    .toList();
            case FUTURE -> bookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .toList();
            case WAITING -> bookings.stream()
                    .filter(b -> b.getStatus() == BookingStatus.WAITING)
                    .toList();
            case REJECTED -> bookings.stream()
                    .filter(b -> b.getStatus() == BookingStatus.REJECTED)
                    .toList();
            case ALL -> bookings;
        };
    }
}
