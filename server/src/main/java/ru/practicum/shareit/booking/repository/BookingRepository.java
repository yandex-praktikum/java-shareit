package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.enums.BookerStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerId(Long bookerId);

    Collection<Booking> findAllByBookerIdAndStartDateTimeIsBeforeAndEndDateTimeIsAfter(Long bookerId, LocalDateTime dateTime1, LocalDateTime dateTime2);

    Collection<Booking> findAllByBookerIdAndStartDateTimeIsAfter(Long bookerId, LocalDateTime dateTime);

    boolean existsByBookerIdAndItemIdAndEndDateTimeIsBefore(Long bookerId, Long itemId, LocalDateTime dateTime);

    Collection<Booking> findAllByBookerIdAndEndDateTimeIsBefore(Long bookerId, LocalDateTime dateTime);

    Collection<Booking> findAllByBookerIdAndStatusEquals(Long bookerId, BookerStatus status);

    Collection<Booking> findAllByItemOwnerId(Long bookerId);

    Collection<Booking> findAllByItemOwnerIdAndStartDateTimeIsBeforeAndEndDateTimeIsAfter(Long ownerId, LocalDateTime dateTime1, LocalDateTime dateTime2);

    Collection<Booking> findAllByItemOwnerIdAndStartDateTimeIsAfter(Long ownerId, LocalDateTime dateTime);

    Collection<Booking> findAllByItemOwnerIdAndEndDateTimeIsBefore(Long ownerId, LocalDateTime dateTime);

    Collection<Booking> findAllByItemOwnerIdAndStatusEquals(Long ownerId, BookerStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.endDateTime <= :nowDate " +
            "AND b.endDateTime = (" +
            "  SELECT MAX(b2.endDateTime) " +
            "  FROM Booking b2 " +
            "  WHERE b2.item.id = :itemId AND b2.endDateTime <= :nowDate)")
    Optional<Booking> findLatestBookingByItemId(@Param("itemId") Long itemId,
                                                @Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.startDateTime >= :nowDate " +
            "AND b.startDateTime = (" +
            "  SELECT MIN(b2.startDateTime) " +
            "  FROM Booking b2 " +
            "  WHERE b2.item.id = :itemId AND b2.startDateTime >= :nowDate)")
    Optional<Booking> findNextBookingByItemId(@Param("itemId") Long itemId,
                                              @Param("nowDate") LocalDateTime nowDate);

}
