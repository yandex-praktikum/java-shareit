package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
              AND b.start <= :now
              AND b.end >= :now
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentByBooker(Long userId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
              AND b.end < :now
            ORDER BY b.start DESC
            """)
    List<Booking> findPastByBooker(Long userId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
              AND b.start > :now
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureByBooker(Long userId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(
            Long userId,
            BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.start <= :now
              AND b.end >= :now
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentByOwner(Long ownerId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.end < :now
            ORDER BY b.start DESC
            """)
    List<Booking> findPastByOwner(Long ownerId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.start > :now
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureByOwner(Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(
            Long ownerId,
            BookingStatus status);

    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(
            Long bookerId,
            Long itemId,
            BookingStatus status,
            LocalDateTime now
    );

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByStartDesc(
            Long itemId,
            LocalDateTime now
    );

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(
            Long itemId,
            LocalDateTime now
    );

}