package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByStartDateDesc(
            long userId,
            Pageable pageable);

    List<Booking> findAllByBooker_IdAndAndEndDateBeforeOrderByStartDateDesc(
            long userId,
            LocalDateTime localDateTime,
            Pageable pageable);

    List<Booking> findAllByBooker_IdAndAndStartDateAfterOrderByStartDateDesc(
            long userId,
            LocalDateTime localDateTime,
            Pageable pageable);

    List<Booking> findAllByBooker_IdAndBookingStatusOrderByStartDateDesc(
            long userId,
            BookingStatus status,
            Pageable pageable);

    List<Booking> findAllByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            long userId,
            LocalDateTime localDateTime1,
            LocalDateTime localDateTime2,
            Pageable pageable);

    List<Booking> findAllByItem_Owner_IdOrderByStartDateDesc(
            long ownerId,
            Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndAndEndDateBeforeOrderByStartDateDesc(
            long ownerId,
            LocalDateTime localDateTime,
            Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndAndStartDateAfterOrderByStartDateDesc(
            long ownerId,
            LocalDateTime localDateTime,
            Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndBookingStatusOrderByStartDateDesc(
            long ownerId,
            BookingStatus status,
            Pageable pageable);

    List<Booking> findAllByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
            long ownerId,
            LocalDateTime localDateTime1,
            LocalDateTime localDateTime2,
            Pageable pageable);

    Optional<Booking> findTop1ByItem_IdAndStartDateIsBeforeAndBookingStatusOrderByStartDateDesc(
            long itemId,
            LocalDateTime localDateTime,
            BookingStatus status);

    Optional<Booking> findTop1ByItem_IdAndStartDateIsAfterAndBookingStatusOrderByStartDateAsc(
            long itemId,
            LocalDateTime localDateTime,
            BookingStatus status);

    Integer countAllByBooker_IdAndItem_IdAndBookingStatusAndStartDateIsBefore(
            long bookerId,
            long itemId,
            BookingStatus status,
            LocalDateTime localDateTime);

}

