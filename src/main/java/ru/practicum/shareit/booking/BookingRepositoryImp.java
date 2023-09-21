package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.MainData;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ReviewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingRepositoryImp implements BookingRepository {
    private final List<Booking> bookings;
    private final List<Review> reviews;
    private Long globalId = 1L;
    private Long reviewGlobalId = 1L;

    public BookingRepositoryImp(MainData mainData) {
        this.bookings = mainData.getBookings();
        this.reviews = mainData.getReviews();
    }

    @Override
    public Booking add(Long userId, Long itemId, Booking booking) {
        booking.setId(globalId);
        globalId++;
        booking.setItemId(itemId);
        booking.setBookerId(userId);
        booking.setStatus(BookingStatus.WAITING);

        checkConflictTime(itemId,booking);
        bookings.add(booking);
        return booking;
    }

    @Override
    public Booking update(Long userId, Long bookingId, BookingDto booking) {
        Booking oldBooking = getBooking(bookingId);
        if (!oldBooking.getItemOwnerId().equals(userId)) {
            throw new ValidationException("юзер не является владельцем предмета");
        }
        if (booking.getStatus() != null) {
            oldBooking.setStatus(booking.getStatus());
        }
        return oldBooking;
    }

    public Booking getBooking(Long bookingId) {
        return bookings.stream().filter(booking -> booking.getId().equals(bookingId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("не найден booking с id - " + bookingId));
    }

    @Override
    public void delete(Long requesterId, Long bookingId) {
        bookings.removeIf(booking -> booking.getId().equals(bookingId) && booking.getBookerId().equals(requesterId));
        reviews.removeIf(review -> review.getBookingId().equals(bookingId) && review.getClientId().equals(requesterId));
    }

    @Override
    public List<Booking> getUsersBookings(Long ownerId) {
        return bookings.stream().filter(booking -> booking.getItemOwnerId().equals(ownerId)).collect(Collectors.toList());
    }

    @Override
    public BookingDto getOne(Long userId, Long bookingId) {
        return toDto(getBooking(bookingId));
    }

    @Override
    public List<BookingDto> getByItemIdAndStatus(Long itemId, BookingStatus status) {
        return bookings.stream().filter(booking -> {
            if (status == null) {
                return booking.getItemId().equals(itemId);
            } else {
                return (booking.getItemId().equals(itemId) && booking.getStatus().equals(status));
            }
            }).map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Review addReview(Long requesterId, Long bookingId, Review review) {
        var booking = getBooking(bookingId);

        checkBooking(booking, requesterId);
        review.setId(reviewGlobalId);
        reviewGlobalId++;
        review.setBookingId(bookingId);
        review.setClientId(requesterId);
        review.setReviewedItemId(booking.getItemId());
        reviews.add(review);
        return review;
    }

    @Override
    public Review updateReview(Long requesterId, Long bookingId, ReviewDto reviewDto) {
        var booking = getBooking(bookingId);
        checkBooking(booking, requesterId);
        Review reviewToUpdate = getReview(bookingId);
        if (reviewDto.getPositive() != null) {
            reviewToUpdate.setPositive(reviewDto.getPositive());
        }
        if (reviewDto.getOpinion() != null) {
            reviewToUpdate.setOpinion(reviewDto.getOpinion());
        }
        return reviewToUpdate;
    }

    @Override
    public void deleteReview(Long requesterId, Long reviewId) {
        reviews.removeIf(review -> review.getClientId().equals(requesterId) && review.getId().equals(reviewId));
    }

    @Override
    public List<ReviewDto> getReviewsForItem(Long itemId) {
        return reviews.stream().filter(review -> review.getReviewedItemId().equals(itemId))
                .map(this::reviewToDto).collect(Collectors.toList());
    }

    private Review getReview(Long bookingId) {
        return reviews.stream().filter(review -> review.getBookingId().equals(bookingId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("не найден отзыв к booking id - " + bookingId));
    }

    private void checkBooking(Booking booking, Long requesterId) {
        if (!booking.getBookerId().equals(requesterId)) {
            throw new ValidationException("пользователь не является заказчиком");
        }
    }

    private ReviewDto reviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setReviewedItemId(review.getReviewedItemId());
        reviewDto.setReviewTime(review.getReviewTime());
        reviewDto.setPositive(review.getPositive());
        reviewDto.setOpinion(review.getOpinion());
        return reviewDto;
    }

    private BookingDto toDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStart(booking.getStart());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItemId(booking.getItemId());
        return bookingDto;
    }

    private void checkConflictTime(Long itemId, Booking booking) {
        var newStart = booking.getStart();
        var newEnd = booking.getEnd();
        for (Booking oldBooking : bookings) {
            if (oldBooking.getItemId().equals(itemId) && oldBooking.getStatus().equals(BookingStatus.APPROVED)) {
                var oldStart = oldBooking.getStart();
                var oldEnd = oldBooking.getEnd();

                if (oldStart.isBefore(newStart) && oldEnd.isAfter(newStart)) {
                    throw new ConflictException("время занято");
                }
                if (oldStart.isBefore(newEnd) && oldEnd.isAfter(newEnd)) {
                    throw new ConflictException("время занято");
                }
                if (newStart.isBefore(oldStart) && newEnd.isAfter(oldEnd)) {
                    throw new ConflictException("время занято");
                }
            }
        }
    }
}
