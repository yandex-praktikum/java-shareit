package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ReviewDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class BookingService {

    BookingRepository bookingRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public Booking add(Long userId, Long itemId, Booking booking) {
        userRepository.containsById(userId);

        var item = itemRepository.getOne(itemId);
        if (!item.getAvailable()) {
            throw new ConflictException("предмет заблокирован. itemId - " + itemId);
        }

        booking.setItemOwnerId(item.getOwnerId());
        checkBookingTime(booking);
        return bookingRepository.add(userId, itemId, booking);
    }

    public Booking update(Long userId, Long bookingId, BookingDto booking) {
        userRepository.containsById(userId);

        return bookingRepository.update(userId, bookingId, booking);
    }

    public void delete(Long requesterId, Long bookingId) {
        userRepository.containsById(requesterId);
        bookingRepository.delete(requesterId, bookingId);
    }

    public List<Booking> getUsersBookings(Long ownerId) {
        userRepository.containsById(ownerId);
        return bookingRepository.getUsersBookings(ownerId);
    }

    public BookingDto getOne(Long userId, Long bookingId) {
        userRepository.containsById(userId);
        return bookingRepository.getOne(userId, bookingId);
    }

    public List<BookingDto> getByItemIdAndStatus(Long itemId, BookingStatus status) {
        return bookingRepository.getByItemIdAndStatus(itemId, status);
    }

    public Review addReview(Long requesterId, Long bookingId, Review review) {
        userRepository.containsById(requesterId);
        return bookingRepository.addReview(requesterId, bookingId, review);
    }

    public Review updateReview(Long requesterId, Long bookingId, ReviewDto reviewDto) {
        userRepository.containsById(requesterId);
        return bookingRepository.updateReview(requesterId, bookingId, reviewDto);
    }

    public void deleteReview(Long requesterId, Long reviewId) {
        userRepository.containsById(requesterId);
        bookingRepository.deleteReview(requesterId, reviewId);
    }
    public List<ReviewDto> getReviewsForItem(Long userId, Long itemId) {
        userRepository.containsById(userId);
        return bookingRepository.getReviewsForItem(itemId);
    }

    private void checkBookingTime(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ValidationException("время введено неверно");
        }
    }
}
