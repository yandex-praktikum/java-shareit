package ru.practicum.shareit.booking.repository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class BookingRepositoryInMemory implements BookingRepository {

    private final Map<Long, Booking> bookingMap = new HashMap<>();

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() != null) {
            log.warn("нельзя создать запрос с id" + booking.getId());
        }
        booking.setId(getNextId());
        bookingMap.put(booking.getId(), booking);
        log.info("создание запроса бронирования id" + booking.getId());
        return booking;
    }

    @Override
    public Booking update(long id, Booking booking) {
        Booking oldBooking = bookingMap.get(id);
        if (booking.getStart() != null) {
            oldBooking.setStart(booking.getStart());
        }
        if (booking.getEnd() != null) {
            oldBooking.setEnd(booking.getEnd());
        }
        if (booking.getStatus() != null) {
            oldBooking.setStatus(booking.getStatus());
        }
        log.info("обновление запроса бронирования id" + oldBooking.getId());

        return oldBooking;
    }

    @Override
    public void del(long itemId) {
        log.info("удаление запроса бронирования id" + itemId);
        bookingMap.remove(itemId);
    }

    @Override
    public Booking getById(long id) {
        log.info("поиск запроса бронирования id" + id);
        return bookingMap.get(id);
    }

    @Override
    public Collection<Booking> findAll() {
        log.info("запрос всех бронирований");
        return bookingMap.values();
    }

    private long getNextId() {
        long currentMaxId = bookingMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
