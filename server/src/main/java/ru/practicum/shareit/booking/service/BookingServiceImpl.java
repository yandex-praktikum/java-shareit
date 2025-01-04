package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.base.service.BaseInDbService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestCreateBookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.enums.BookerStatus;
import ru.practicum.shareit.booking.util.enums.State;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static java.lang.String.format;

@Slf4j
@Service
public class BookingServiceImpl extends BaseInDbService<Booking, BookingRepository> implements BookingService {
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, ItemService itemService, UserService userService) {
        super(repository, Booking.class);
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public BookingDto create(Long userId, RequestCreateBookingDto bookingDto) {
        User booker = userService.findById(userId);
        Item item = itemService.findById(bookingDto.getItemId());
        if (!item.isAvailable())
            throw new ValidationException(format("Item{id=%d} не активен", item.getId()));
        Booking booking = BookingMapper.fromDto(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookerStatus.WAITING);

        Booking savedBooking = super.save(booking);
        log.info("Booking{id={}}: Успешно создан", savedBooking.getId());
        return this.toDto(savedBooking);
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        Booking storageBooking = this.findById(bookingId);
        if (!userService.existsById(userId))
            throw new ValidationException(String.format("User{id=%d} не существует", userId));

        if (!storageBooking.getItem().getOwner().getId().equals(userId))
            throw new AccessDeniedException(
                    format("User{id=%d} не имеет доступа на изменение статуса Booking{id=%d}", userId, bookingId), 403);

        storageBooking.setStatus(approved ? BookerStatus.APPROVED : BookerStatus.REJECTED);

        Booking savedBooking = super.save(storageBooking);
        log.info("Booking{id={}}: Статус обновлен", savedBooking.getId());
        return this.toDto(savedBooking);
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking storageBooking = super.findById(bookingId);
        userService.checkExistsById(userId);

        User booker = storageBooking.getBooker();
        User owner = storageBooking.getItem().getOwner();

        if (!(booker.getId().equals(userId) || owner.getId().equals(userId)))
            throw new AccessDeniedException(
                    format("User{id=%d} не имеет доступа на просмотр Booking{id=%d}", userId, bookingId), 403);

        log.info("Booking{id={}}: Успешно показан пользователю User{id={}}", bookingId, userId);
        return this.toDto(storageBooking);
    }

    @Override
    public Collection<BookingDto> getAllByUserId(Long userId, String state) {
        State condition = State.fromString(state);
        Collection<Booking> result;
        switch (condition) {
            case ALL -> result = repository.findAllByBookerId(userId);
            case CURRENT -> {
                var dateTime = LocalDateTime.now();
                result = repository.findAllByBookerIdAndStartDateTimeIsBeforeAndEndDateTimeIsAfter(userId, dateTime, dateTime);
            }
            case FUTURE -> result = repository.findAllByBookerIdAndStartDateTimeIsAfter(userId, LocalDateTime.now());
            case PAST -> result = repository.findAllByBookerIdAndEndDateTimeIsBefore(userId, LocalDateTime.now());
            case WAITING -> result = repository.findAllByBookerIdAndStatusEquals(userId, BookerStatus.WAITING);
            case REJECTED -> result = repository.findAllByBookerIdAndStatusEquals(userId, BookerStatus.REJECTED);
            default -> throw new ValidationException(format("Переданный параметр state=%s не поддерживается", state));
        }

        return this.toDto(result);
    }

    @Override
    public Collection<BookingDto> getAllByItemOwnerId(Long userId, String state) {
        userService.checkExistsById(userId);

        State condition = State.fromString(state);
        Collection<Booking> result;
        switch (condition) {
            case ALL -> result = repository.findAllByItemOwnerId(userId);
            case CURRENT -> {
                var dateTime = LocalDateTime.now();
                result = repository.findAllByItemOwnerIdAndStartDateTimeIsBeforeAndEndDateTimeIsAfter(userId, dateTime, dateTime);
            }
            case FUTURE -> result = repository.findAllByItemOwnerIdAndStartDateTimeIsAfter(userId, LocalDateTime.now());
            case PAST -> result = repository.findAllByItemOwnerIdAndEndDateTimeIsBefore(userId, LocalDateTime.now());
            case WAITING -> result = repository.findAllByItemOwnerIdAndStatusEquals(userId, BookerStatus.WAITING);
            case REJECTED -> result = repository.findAllByItemOwnerIdAndStatusEquals(userId, BookerStatus.REJECTED);
            default -> throw new ValidationException(format("Переданный параметр state=%s не поддерживается", state));
        }
        return this.toDto(result);
    }

    private BookingDto toDto(Booking booking) {
        return BookingMapper.toDto(booking);
    }

    private Collection<BookingDto> toDto(Collection<Booking> booking) {
        return BookingMapper.toDto(booking);
    }
}
