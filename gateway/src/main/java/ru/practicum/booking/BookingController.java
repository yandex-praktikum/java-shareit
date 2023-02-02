package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.exception.UnknownStatusException;
import ru.practicum.booking.dto.BookItemRequestDto;
import ru.practicum.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader(Constants.USER_ID_HEADER) long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBookingById(userId, bookingId);
	}
	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingByOwner(
			@RequestHeader(Constants.USER_ID_HEADER) Long ownerId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", required = false) Integer from,
			@Positive @RequestParam(name = "size", required = false) Integer size) throws UnknownStatusException {
		log.info("Get booking, ownerId={}", ownerId);
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnknownStatusException("Unknown state: " + stateParam));
		return bookingClient.getBookingByOwner(ownerId, state, from, size);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookings(
			@RequestHeader(Constants.USER_ID_HEADER) long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", required = false) Integer from,
			@Positive @RequestParam(name = "size", required = false) Integer size) throws UnknownStatusException {
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnknownStatusException("Unknown state: " + stateParam));
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(Constants.USER_ID_HEADER) long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.createBooking(userId, requestDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateBooking(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
												@PathVariable Long bookingId,
												@RequestParam(required = false) Boolean approved) {
		log.info("Update booking {}, userId={}", bookingId, userId);
		return bookingClient.updateBookingStatus(userId, bookingId, approved);
	}
}
