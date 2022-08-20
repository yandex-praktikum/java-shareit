package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b " +
            "where b.item.id = ?1 " +
            "and b.item.owner.id = ?2 " +
            "and b.end < current_timestamp " +
            "order by b.end desc")
    Optional<Booking> findLastBooking(Long itemId, Long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.id = ?1 and b.item.owner.id = ?2 and b.start > current_timestamp " +
            "order by b.start")
    Optional<Booking> findNextBooking(Long itemId, Long ownerId);

    @Query("select b from Booking b where b.booker.id = ?1 order by b.id desc")
    List<Booking> findAllByBooker(Long id);

    @Query("select b " +
            "from Booking b " +
            "where b.booker.id = ?1 and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllByBookerAndFutureState(Long id);

    @Query("select b " +
            "from Booking b " +
            "where b.booker.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllByBookerAndPastState(Long id);

    @Query("select b " +
            "from Booking b " +
            "where b.booker.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    Optional<Booking> findByBookerIdAndPastState(Long bookerId);

    @Query("select b " +
            "from Booking b " +
            "where b.booker.id = ?1 and b.start < current_timestamp and b.end > current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllByBookerAndCurrentState(Long id);

    @Query("select b from Booking b where b.item.owner.id = ?1 order by b.start desc")
    List<Booking> findAllByOwner(Long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.owner.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndPastState(Long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.owner.id = ?1 and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndFutureState(Long ownerId);

    @Query("select b " +
            "from Booking b " +
            "where b.item.owner.id = ?1 and b.start < current_timestamp and b.end > current_timestamp " +
            "order by b.start desc")
    List<Booking> findAllByOwnerAndCurrentState(Long ownerId);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, Status status);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2")
    List<Booking> findAllByOwnerIdAndStatus(Long ownerId, Status status);


}
