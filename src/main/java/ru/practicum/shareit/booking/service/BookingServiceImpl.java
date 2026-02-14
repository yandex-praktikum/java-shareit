package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.storage.InMemoryBookingStorage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final InMemoryBookingStorage bookingStorage;
    private final InMemoryUserStorage userStorage;
    private final InMemoryItemStorage itemStorage;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long bookerId) {
        User booker = userStorage.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + bookerId + " не найден"));

        Item item = itemStorage.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDto.getItemId() + " не найдена"));

        if (!Boolean.TRUE.equals(item.getAvailable())) {
            throw new ValidationException("Вещь с id " + bookingDto.getItemId() + " недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(bookerId)) {
            throw new AccessDeniedException("Пользователь не может бронировать свою собственную вещь");
        }

        Booking booking = BookingMapper.fromBookingDto(bookingDto, item, booker);
        booking.setStatus(BookingStatus.WAITING);
        Booking createdBooking = bookingStorage.create(booking);
        return BookingMapper.toBookingDto(createdBooking);
    }

    @Override
    public BookingDto updateBooking(Long bookingId, BookingDto bookingDto) {
        Booking existingBooking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        if (bookingDto.getStart() != null) {
            existingBooking.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            existingBooking.setEnd(bookingDto.getEnd());
        }
        if (bookingDto.getStatus() != null) {
            existingBooking.setStatus(bookingDto.getStatus());
        }

        Booking updatedBooking = bookingStorage.update(existingBooking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        return BookingMapper.toBookingDto(booking);
    }
}
