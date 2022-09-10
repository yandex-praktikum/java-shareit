package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.StateEnum;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BadRequestException;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    public BookingUpdDto create(@RequestBody BookingCreateDto bookingCreateDto,
                                @RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return bookingService.create(bookingCreateDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingUpdDto update(@RequestParam boolean approved,
                                @RequestHeader(Constants.USER_ID_HEADER) long userId,
                                @PathVariable long bookingId) {
        return bookingService.update(approved, bookingId, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingUpdDto get(@PathVariable long bookingId,
                             @RequestHeader(Constants.USER_ID_HEADER) long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingUpdDto> getAll(@RequestParam(value = "state", required = false, defaultValue = "ALL") StateEnum state,
                                            @RequestHeader(Constants.USER_ID_HEADER) long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "25") int size) {
        if (from < 0 || size < 0) {
            throw new BadRequestException("некорректное значение страницы");
        }

        return bookingService.getAllByBookerId(state, userId, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingUpdDto> getAllWhereOwnerOfItems(@RequestParam(value = "state", required = false, defaultValue = "ALL") StateEnum state,
                                                             @RequestHeader(Constants.USER_ID_HEADER) long ownerId,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "25") int size) {
        if (from < 0 || size < 0) {
            throw new BadRequestException("некорректное значение страницы");
        }

        return bookingService.getAllWhereOwnerOfItems(state, ownerId, from, size);
    }


    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable long bookingId) {
        bookingService.delete(bookingId);
    }
}
