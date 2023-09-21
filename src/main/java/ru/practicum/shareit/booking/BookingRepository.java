package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ReviewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;

import java.util.List;

public interface BookingRepository {

    Booking add(Long userId, Long itemId, Booking booking);
    Booking update(Long userId, Long itemId, BookingDto booking);
    void delete(Long requesterId, Long bookingId);
    List<Booking> getUsersBookings(Long ownerId);
    BookingDto getOne(Long userId, Long bookingId);
    List<BookingDto> getByItemIdAndStatus(Long itemId, BookingStatus status);
    Review addReview(Long requesterId, Long bookingId, Review review);
    Review updateReview(Long requesterId, Long bookingId, ReviewDto reviewDto);
    void deleteReview(Long requesterId, Long reviewId);
    List<ReviewDto> getReviewsForItem(Long itemId);
}
