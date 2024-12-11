package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Collection<Booking> findAllByItemIdAndStartAfterOrderByStartAsc(int itemId, LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartAsc(Collection<Integer> itemIds, LocalDateTime time);

    Collection<Booking> findAllByBookerIdOrderByStartDesc(int bookerId);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int bookerId, LocalDateTime time);

    Optional<Booking> findByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(int bookerId, int itemId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int bookerId, LocalDateTime time);

    @Query("SELECT b from Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "AND ?2 > b.start " +
            "AND ?2 < b.end ")
    Collection<Booking> findAllCurrentBookings(int bookerId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);

    Collection<Booking> findAllByItemIdInAndStatusOrderByStartDesc(Collection<Integer> itemIds, BookingStatus status);

    Collection<Booking> findAllByItemIdInOrderByStartDesc(Collection<Integer> itemIds);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartDesc(Collection<Integer> itemIds, LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndEndBeforeOrderByStartDesc(Collection<Integer> itemIds, LocalDateTime time);

    @Query("SELECT b from Booking AS b " +
            "WHERE b.item.id in ?1 " +
            "AND ?2 > b.start " +
            "AND ?2 < b.end ")
    Collection<Booking> findAllCurrentBookings(Collection<Integer> itemIds, LocalDateTime time);
}
