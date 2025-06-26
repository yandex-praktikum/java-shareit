package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
        SELECT b FROM Booking b
        WHERE b.item.id = :itemId
          AND b.end < :now
          AND b.status = 'APPROVED'
        ORDER BY b.end DESC
        LIMIT 1
    """)
    Optional<Booking> findLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("""
        SELECT b FROM Booking b
        WHERE b.item.id = :itemId
          AND b.start >= :now
          AND b.status = 'APPROVED'
        ORDER BY b.start ASC
        LIMIT 1
    """)
    Optional<Booking> findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE b.booker.id = :id " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingByStateALL(@Param("id") Long id);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE b.booker.id = :id " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingByStateStatus(@Param("id") Long id, @Param("status") BookingStatus status);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE b.booker.id = :id " +
            "AND b.status = 'APPROVED' " +
            "AND b.start <= :currentTime " +
            "AND b.end >= :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingByStateCurrent(@Param("id") Long id,
                                           @Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE b.booker.id = :id " +
            "AND b.status = 'APPROVED' " +
            "AND b.start >= :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingByStateFuture(@Param("id") Long id,
                                           @Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE b.booker.id = :id " +
            "AND b.status = 'APPROVED' " +
            "AND b.end <= :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingByStatePast(@Param("id") Long id,
                                          @Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = :id " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingAllItemsByStateALL(@Param("id") Long id);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = :id " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingAllItemsByStateStatus(@Param("id") Long id, @Param("status") BookingStatus status);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = :id " +
            "AND b.status = 'APPROVED' " +
            "AND b.start <= :currentTime " +
            "AND b.end >= :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingAllItemsByStateCurrent(@Param("id") Long id,
                                           @Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = :id " +
            "AND b.status = 'APPROVED' " +
            "AND b.start >= :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingAllItemsByStateFuture(@Param("id") Long id,
                                          @Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE i.owner.id = :id " +
            "AND b.status = 'APPROVED' " +
            "AND b.end <= :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingAllItemsByStatePast(@Param("id") Long id,
                                        @Param("currentTime")LocalDateTime currentTime);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN FETCH b.item i " +
            "JOIN FETCH i.owner " +
            "WHERE b.booker.id = :user AND i.id = :item " +
            "AND b.status = 'APPROVED' " +
            "AND b.end <= :currentTime " +
            "ORDER BY b.start DESC")
    Optional<Booking> getPostBooking(@Param("item") Long item,
                                 @Param("user") Long user,
                                 @Param("currentTime")LocalDateTime currentTime);
}