package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserService userService;
    private BookingMapper bookingMapper;

    @Override
    public List<BookingUpdDto> getAllByBookerId(StateEnum state, long bookerId, int from, int size) {
        userService.getUserById(bookerId);
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start"));

        switch (state) {
            case PAST:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now, pageable));
            case WAITING:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING, pageable));
            case FUTURE:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .getFutureBookings(bookerId, now, pageable));
            case REJECTED:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED, pageable));
            case CURRENT:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .getCurrentBookings(bookerId, now, pageable));
            default:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findByBookerIdOrderByStartDesc(bookerId, pageable));
        }
    }

    @Override
    public List<BookingUpdDto> getAllWhereOwnerOfItems(StateEnum state, long ownerId, int from, int size) {
        userService.getUserById(ownerId);
        List<Item> items = itemRepository.getAllByOwnerId(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start"));
        if (items == null || items.size() == 0) {
            return new ArrayList<>();
        }

        switch (state) {
            case PAST:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now, pageable));
            case WAITING:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING, pageable));
            case FUTURE:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now, pageable));
            case REJECTED:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED, pageable));
            case CURRENT:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .findAllCurrentBookings(ownerId, now, pageable));
            default:
                return bookingMapper.toBookingUpdDtoList(bookingRepository
                        .getBookingsByItemOwnerIdOrderByStartDesc(ownerId, pageable));
        }
    }

    @Override
    public BookingUpdDto getBookingById(long bookingId, long userId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (booking.isPresent()) {
            if (booking.get().getBooker().getId() == userId || booking.get().getItem().getOwnerId() == userId) {
                return bookingMapper.toBookingUpdDto(booking.get());
            } else throw new BookingNotFoundException("Бронирование не найдено");
        } else {
            throw new BookingNotFoundException("Бронирование не найдено");
        }
    }

    @Override
    public BookingUpdDto create(BookingCreateDto bookingDto, long userId) {
        Booking booking = bookingMapper.toBookingFromCreatedDto(bookingDto, userId);
        Optional<Item> item = itemRepository.findById(booking.getItem().getId());

        if (item.isPresent() && item.get().getAvailable()) {
            if (!Objects.equals(item.get().getOwnerId(), booking.getBooker().getId())) {
                return bookingMapper.toBookingUpdDto(bookingRepository.save(booking));
            } else {
                throw new ItemNotFoundException("Вы неможете создать бронирование для своей вещи");
            }
        } else {
            throw new NotAvailableException("Вещь недоступна");
        }
    }

    @Override
    public void delete(long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public BookingUpdDto update(boolean approved, long bookingId, long userId) {
        Booking booking;
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        if (bookingOptional.isPresent()) {
            booking = bookingOptional.get();
        } else {
            throw new BookingNotFoundException("бронирование не найдено");
        }

        if (userId != booking.getItem().getOwnerId()) {
            throw new BookingNotFoundException("Недостаточно прав для просмотра бронирования");
        }

        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new NotAvailableException("Бронирование уже одобрено");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);

        return bookingMapper.toBookingUpdDto(booking);
    }
}
