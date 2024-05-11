package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByStartDesc(int userId, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(int userId, LocalDateTime now, LocalDateTime now1, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int userId, BookingStatus bookingStatus, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(int ownerId, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(int ownerId, LocalDateTime now, LocalDateTime now1, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(int userId, LocalDateTime now, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(int userId, LocalDateTime now, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(int userId, BookingStatus bookingStatus, PageRequest pageRequest);

    List<Booking> findAllByItemIdAndStatusNotOrderByStartAsc(int itemId, BookingStatus bookingStatus);

    List<Booking> findAllByItemIdAndBookerIdAndEndBefore(int itemId, int bookerId, LocalDateTime now);
}
