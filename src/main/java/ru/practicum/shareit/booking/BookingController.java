package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.exception.BookingDtoBadStateException;
import ru.practicum.shareit.booking.exception.ErrorResponse;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validation.ValidateState;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    BookingDtoToUser create(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                            @RequestBody @Valid BookingDto bookingDto) {
        log.info("create booking");
        return bookingService.create(userId, bookingDto.getItemId(), bookingDto);
    }

    @PatchMapping("/{bookingId}")
    BookingDtoToUser approveStatus(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam boolean approved) {
        return bookingService.approveStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDtoToUser getBookingById(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId) {
        log.info("get booking id={}", bookingId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping()
    List<BookingDtoState> getBookingCurrentUser(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @Valid @RequestParam(defaultValue = "1") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("get booking current user id ={}", userId);
        State stateEnum = ValidateState.validateStatus(state);
        return bookingService.getBookingCurrentUser(userId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    List<BookingDtoState> getBookingCurrentOwner(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "1") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("get booking current owner id ={}", userId);
        State stateEnum = ValidateState.validateStatus(state);
        return bookingService.getBookingCurrentOwner(userId, stateEnum, from, size);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(BookingDtoBadStateException e) {
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");
    }
}
