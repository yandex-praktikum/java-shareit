package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.ValidateBookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ValidateBookingController validateBookingController;

    @Override
    public Booking saveBooking(long userId, BookingDto bookingDto) {
        validateBookingController.validateBookingDto(bookingDto);
        if (userService.findByIdUser(userId) == null) {
            log.warn("нет такого пользователя");
            throw new NotDataException("нет такого пользователя");
        }
        if (itemService.getItemByItemId(bookingDto.getItem()) == null) {
            log.warn("нет такой вещи");
            throw new NotDataException("нет такой вещи");
        }
        if (itemService.getItemByItemId(bookingDto.getItem()).getOwner() == userId) {
            log.warn("нельзя бронировать свою вещь");
            throw new NotDataException("нельзя бронировать свою вещь");
        }
        if (!itemService.getItemByItemId(bookingDto.getItem()).getAvailable()) {
            log.warn("нельзя бронировать");
            throw new NotDataException("нельзя бронировать");
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setBooker(userId);
        booking.setStatus(Status.WAITTING);
        booking = bookingRepository.save(booking);
        log.info("сохранение запроса бронирование id" + booking.getId());
        return booking;
    }

    @Override
    public Booking updateBooking(long userId, long itemId, BookingDto bookingDto) {

        Booking booking = bookingRepository.getById(itemId);

        if (userService.findByIdUser(userId) == null) {
            log.warn("нет такого пользователя");
            throw new NotDataException("нет такого пользователя");
        }
        if (booking == null) {
            log.warn("нет такого запроса");
            throw new NotDataException("нет такого запроса");
        }


        System.out.println(userId);
        System.out.println(booking);
        System.out.println(bookingDto);


        if (booking.getBooker() != userId && itemService.getItemByItemId(booking.getItem()).getOwner() != userId) {
            log.warn("вы не собственник запроса и не владелиц вещи");
            throw new NotDataException("вы не собственник запроса и не владелиц вещи");
        }
        if (booking.getBooker() == userId && itemService.getItemByItemId(booking.getItem()).getOwner() != userId) {
            if (booking.getStatus() != bookingDto.getStatus() && bookingDto.getStatus() != Status.CANCELED) {
                log.warn("нельзя поставить такой статус бронирования");
                throw new NotDataException("нельзя поставить такой статус бронирования");
            }
        }
        if (booking.getBooker() != userId && itemService.getItemByItemId(booking.getItem()).getOwner() == userId) {
            if (booking.getStatus() != bookingDto.getStatus() && bookingDto.getStatus() == Status.CANCELED) {
                log.warn("нельзя поставить такой статус бронирования");
                throw new NotDataException("нельзя поставить такой статус бронирования");
            }
        }
        log.info("обновили запрос");

        return bookingRepository.update(itemId, BookingMapper.mapToBooking(bookingDto));
    }

    @Override
    public void delBooking(long userId, long itemId) {

        Booking booking = bookingRepository.getById(itemId);
        if (booking == null) {
            log.warn("нет такого запроса");
            throw new NotDataException("нет такого запроса");
        }
        if (booking.getBooker() != userId) {
            log.warn("не собственник запроса");
            throw new NotDataException("не собственник запроса");
        }
        bookingRepository.del(itemId);

    }

    @Override
    public BookingDto geyBookingById(long id) {
        Booking booking = bookingRepository.getById(id);
        if (booking == null) {
            log.warn("пустой запрос");
            throw new NotDataException("пустой запрос");
        }
        log.info("получаем запрос бронирования по id" + id);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> findAllBooking() {
        log.info("запрос всех запросов бронирования");
        return bookingRepository.findAll().stream()
                .map(booking -> BookingMapper.mapToBookingDto(booking))
                .collect(Collectors.toList());
    }
}
