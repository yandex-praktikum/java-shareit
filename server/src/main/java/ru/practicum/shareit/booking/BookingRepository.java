package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select b from Booking b join Item i on b.item=i.id where b.id=?1 and (b.bookerId=?2 or i.owner=?2)")
    Optional<Booking> getById(Integer bookingId, Integer userId);

    @Query("select distinct b from Booking b join Item i on i.id=b.item and b.bookerId=?1 and b.status=?2 "
            + "and b.bookerId <> i.owner order by b.start desc")
    Page<Booking> getBookingsByUserAndStatus(Integer userId, Status status, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item and b.bookerId=?1 and b.bookerId <> i.owner "
            + "order by b.start desc")
    Page<Booking> getBookingsByUser(Integer userId, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 order by b.start desc")
    Page<Booking> getAllByOwner(Integer userId, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 and b.status=?2 order by b.start desc")
    Page<Booking> getAllByOwnerAndStatus(Integer userId, Status status, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 and b.end>?2 order by b.start desc")
    Page<Booking> getByOwnerFuture(Integer userId, LocalDateTime date, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 and b.end<?2 order by b.start desc")
    Page<Booking> getByOwnerPast(Integer userId, LocalDateTime date, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item "
            + "where i.owner=?1 and b.end>?2 and b.start<?2 order by b.start desc")
    Page<Booking> getByOwnerCurrent(Integer userId, LocalDateTime date, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item and i.id=?1 and b.end<?2")
    Booking getLastBooking(Integer itemId, LocalDateTime dateTime);

    @Query("select b from Booking b join Item i on i.id=b.item and i.id=?1 and b.start>?2 and b.end>?2")
    Booking getNextBooking(Integer itemId, LocalDateTime dateTime);

    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Integer userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsAfterOrderByStartDesc(Integer userId, LocalDateTime date, Pageable pageable);

    @Query("select b from Booking b where b.bookerId=?1 and b.start<?2 and b.end>?2")
    Page<Booking> getByUserCurrent(Integer userId, LocalDateTime date, Pageable pageable);

    @Query("select b from Booking b where b.bookerId=?1 and b.item=?2 ")
    Collection<Booking> getByBookerAndItem(Integer bookerId, Integer itemId);

    @Query("select count (b) from Booking b join Item i on i.id=b.item and b.bookerId=?1 " +
            "and b.bookerId <> i.owner")
    Integer getCountBookingsByUser(Integer userId);

}
