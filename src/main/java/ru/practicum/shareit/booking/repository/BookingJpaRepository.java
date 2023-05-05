package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBookerId(int userId, Pageable page);

    List<Booking> findByBookerIdAndEndIsBefore(int bookerId, LocalDateTime end, Pageable page);

    List<Booking> findByBookerIdAndStartIsAfter(int userId, LocalDateTime start, Pageable page);

    List<Booking> findByBookerIdAndStatus(int userId, Status status, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.booker.id = ?1) " +
            "and (b.start < current_timestamp) " +
            " and (b.end > current_timestamp )")
    List<Booking> findCurrentByBookerId(int userId, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.item.owner.id = ?1)")
    List<Booking> findByOwnerId(int userId, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.item.owner.id = ?1) " +
            "and (b.start < current_timestamp) " +
            " and (b.end > current_timestamp )")
    List<Booking> findCurrentByOwnerId(int userId, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.item.owner.id = ?1) " +
            "and (b.start < current_timestamp) " +
            " and (b.end < current_timestamp )")
    List<Booking> findPastByOwnerId(int userId, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.item.owner.id = ?1) " +
            "and (b.start > current_timestamp) " +
            " and (b.end > current_timestamp )")
    List<Booking> findFutureByOwnerId(int userId, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.item.owner.id = ?1) " +
            "and (b.status = 'WAITING')")
    List<Booking> findWaitingByOwnerId(int userId, Pageable page);

    @Query(" select b from Booking b " +
            "where (b.item.owner.id = ?1) " +
            "and (b.status = 'REJECTED')")
    List<Booking> findRejectedByOwnerId(int userId, Pageable page);

    List<Booking> findByItemId(int itemId);

    @Query(value = "select * from BOOKINGS b " +
            "where (ITEM_ID = ?1) " +
            "and (START_DATE < current_timestamp) " +
            "and (STATUS = 'APPROVED') " +
            "order by END_DATE desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findLastBookingForItem(int id);

    @Query(value = "select * from BOOKINGS b " +
            "where (ITEM_ID = ?1) " +
            "and (START_DATE > current_timestamp) " +
            "and (STATUS = 'APPROVED') " +
            "order by START_DATE asc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findNextBookingForItem(int id);

    List<Booking> findByBookerIdAndItemId(int userId, int itemId, Sort sort);
}
