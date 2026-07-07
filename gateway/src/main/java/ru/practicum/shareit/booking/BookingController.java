package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> findBookingById(@PathVariable Long bookingId,
												  @RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("GET /bookings/{} - userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> findAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
														@RequestParam(defaultValue = "ALL") String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("GET /bookings - userId={}, state={}", userId, state);
		return bookingClient.getBookingsByState(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long ownerId,
												   @RequestParam(defaultValue = "ALL") String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("GET /bookings/owner - ownerId={}, state={}", ownerId, state);
		return bookingClient.getOwnerBookings(ownerId, state);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
												@Valid @RequestBody BookItemRequestDto dto) {
		log.info("POST /bookings - userId={}, dto={}", userId, dto);
		return bookingClient.bookItem(userId, dto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateStatus(@PathVariable Long bookingId,
											   @RequestHeader("X-Sharer-User-Id") Long ownerId,
											   @RequestParam boolean approved) {
		log.info("PATCH /bookings/{} - ownerId={}, approved={}", bookingId, ownerId, approved);
		return bookingClient.updateBookingStatus(bookingId, ownerId, approved);
	}
}
