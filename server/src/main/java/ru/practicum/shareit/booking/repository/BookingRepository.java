package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdOrderByStartDesc(Long ownerId);

    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime now);
}