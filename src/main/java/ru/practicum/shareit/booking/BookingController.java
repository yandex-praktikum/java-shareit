package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ReviewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/{itemId}")
    public Booking add(@RequestHeader("X-Sharer-User-Id") Long bookerId, @PathVariable Long itemId,
                       @Valid @RequestBody Booking booking) {
        return bookingService.add(bookerId, itemId, booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking update(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long bookingId,
                           @Valid @RequestBody BookingDto booking) {
        return bookingService.update(ownerId, bookingId, booking);
    }

    @DeleteMapping("/{bookingId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long requesterId, @PathVariable Long bookingId) {
        bookingService.delete(requesterId, bookingId);
    }

    @GetMapping("/myBookings")
    public List<Booking> getUsersBookings(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingService.getUsersBookings(ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getOne(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getOne(userId, bookingId);
    }

    @GetMapping
    public  List<BookingDto> getByItemIdAndStatus(@RequestParam Long itemId, @RequestParam(required = false) BookingStatus status) {
        return bookingService.getByItemIdAndStatus(itemId, status);
    }

    @PostMapping("/{bookingId}/review")
    public Review addReview(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                            @PathVariable Long bookingId, @Valid @RequestBody Review review) {
        return bookingService.addReview(requesterId, bookingId, review);
    }

    @PatchMapping("/{bookingId}/review")
    public Review updateReview(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                               @PathVariable Long bookingId, @RequestBody ReviewDto reviewDto) {
        return bookingService.updateReview(requesterId, bookingId, reviewDto);
    }

    @DeleteMapping("/review/{reviewId}")
    public void deleteReview(@RequestHeader("X-Sharer-User-Id") Long requesterId, @PathVariable Long reviewId) {
        bookingService.deleteReview(requesterId, reviewId);
    }

    @GetMapping("/review/{itemId}")
    public List<ReviewDto> getReviewsForItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return bookingService.getReviewsForItem(userId, itemId);
    }

}
