package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    @Query("""
            SELECT b FROM Booking b
                WHERE b.itemId = ?1
                AND b.end < ?2
                ORDER BY b.end DESC
            """)
    List<Booking> findLastBookingForItem(Long itemId, LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
                WHERE b.itemId = ?1
                AND b.start > ?2
                AND b.status = 'APPROVED'
                ORDER BY b.start ASC
            """)
    List<Booking> findNextBookingForItem(Long itemId, LocalDateTime now);


    @Query("""
            SELECT b FROM Booking b
                WHERE b.bookerId = ?1
                AND b.itemId = ?2
                AND b.end < ?3
                AND b.status = 'APPROVED'
            """)
    List<Booking> findCompletedBookingsByBookerAndItem(Long bookerId, Long itemId, LocalDateTime now);
}

