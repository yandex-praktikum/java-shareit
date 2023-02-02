package ru.practicum.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.id =?1")
    Booking findBooking(Long bookingId);

    @Query("SELECT b FROM Booking b WHERE b.id =?1 AND (b.booker.id =?2 OR b.item.ownerId =?2)")
    Booking findBookingById(Long bookingId, Long userId);

    @Query("select b from Booking b where b.booker.id =?1 order by b.id desc")
    List<Booking> findBookingsByBooker(Long userId, Pageable pageable);

    @Query("select b from Booking b where b.booker.id =?1 and b.status = ?2 order by b.id desc")
    List<Booking> findBookingsByBookerWithState(Long userId, Status state, Pageable pageable);

    @Query("select b from Booking b where b.booker.id =?1 and b.end > current_timestamp order by b.id desc")
    List<Booking> findBookingsByBookerFuture(Long userId, Pageable pageable);

    @Query("select b from Booking b where b.booker.id =?1 and b.end > current_timestamp " +
            "and b.start < current_timestamp order by b.id desc")
    List<Booking> findBookingsByBookerCurrent(Long userId, Pageable pageable);

    @Query("select b from Booking b where b.booker.id =?1 and b.end < current_timestamp " +
            "order by b.id desc")
    List<Booking> findBookingsByBookerPast(Long userId, Pageable pageable);

    @Query("select b from Booking b where b.booker.id = ?1 and b.item.id = ?2 " +
            "and b.status <> ru.practicum.booking.Status.REJECTED order by b.id desc")
    List<Booking> findBookingsByItemAndBooker(Long userId, Long itemId);

    @Query("select b from Booking b where b.item.ownerId =?1 and b.end > current_timestamp " +
            "and b.start < current_timestamp order by b.id desc")
    List<Booking> findBookingsByOwnerCurrent(Long ownerId, Pageable pageable);

    @Query("select b from Booking b where b.item.ownerId =?1 and b.end < current_timestamp " +
            "order by b.id desc")
    List<Booking> findBookingsByOwnerPast(Long ownerId, Pageable pageable);

    @Query("select b from Booking b where b.item.ownerId =?1 order by b.id desc")
    List<Booking> findBookingsByOwner(Long ownerId, Pageable pageable);

    @Query("select b from Booking b where b.item.ownerId =?1 AND " +
            "b.status = ?2 order by b.id desc")
    List<Booking> findBookingsByOwnerWithState(Long ownerId, Status status, Pageable pageable);

    @Query("select b from Booking b where b.item.ownerId =?1 and b.end > current_timestamp order by b.id desc")
    List<Booking> findBookingsByOwnerFuture(Long ownerId, Pageable pageable);

    @Query("select b from Booking b where b.item.id = ?1 and b.item.ownerId = ?2 " +
            "and b.status <> ru.practicum.booking.Status.REJECTED  order by b.start asc")
    List<Booking> findBookingsByItemAndOwner(Long itemId, Long ownerId);

}
