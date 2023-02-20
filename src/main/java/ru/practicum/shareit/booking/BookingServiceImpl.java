package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StatusDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final UserService userService;

    private final ItemService itemService;

    private final BookingMapper bookingMapper;

    private void checkValidUser(Integer id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private void checkOnValidBeforeAdd(Booking booking, Integer ownerId) {
        if (itemRepository.findById(booking.getItem()).isEmpty()
                || userRepository.findById(booking.getBookerId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (booking.getBookerId() == null || booking.getStart() == null || booking.getEnd() == null
                || booking.getItem() == null
                || !itemRepository.findById(booking.getItem())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getAvailable()
                || booking.getEnd().isBefore(booking.getStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (itemRepository.findById(booking.getItem()).orElseThrow().getOwner().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    private Item findItem(Integer itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public BookingDto add(BookingDto bookingDto, Integer userId) {
        Booking booking = bookingMapper.toBooking(bookingDto, userId);
        checkOnValidBeforeAdd(booking, userId);
        booking.setStatus(Status.WAITING);
        log.info("добавлено бронирование /{}/", booking);
        bookingRepository.save(booking);
        Item item = itemService.getById(booking.getItem());
        User user = userService.getById(booking.getBookerId());
        return bookingMapper.toDto(booking, item, user);
    }

    @Override
    public BookingDto updApprove(Integer bookingId, Boolean approved, Integer userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("updApprove>>>>>booking={}, userId={}", booking.toString(), userId);
        if (booking.getStatus() == Status.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!findItem(booking.getItem()).getOwner().equals(userId)) {
            log.info("updApprove>>NotFoundItem>>>");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Item item = itemService.getById(booking.getItem());
        User user = userService.getById(booking.getBookerId());
        log.info("изменен статус бронирования /{}/", booking);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking, item, user);
    }

    @Override
    public BookingDto findById(Integer bookingId, Integer userId) {
        checkValidUser(userId);
        Booking booking = bookingRepository.getById(bookingId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Item item = itemService.getById(booking.getItem());
        User user = userService.getById(booking.getBookerId());
        log.info("запрошено бронирование /{}/ владельца /{}/", bookingId, userId);
        return bookingMapper.toDto(booking, item, user);
    }

    private Collection<Booking> findAllByUserAndStatus(Integer userId, Status stat) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ со статусом /{}/", userId, stat);
        return bookingRepository.getBookingsByUserAndStatus(userId, stat);
    }

    @Override
    public Collection<BookingDto> findAllByUser(Integer userId, StatusDto state) {
        checkValidUser(userId);
        ArrayList<BookingDto> listDto = new ArrayList<>();
        switch (state) {
            case ALL:
                for (Booking booking : bookingRepository.getBookingsByUser(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                log.info("запрошены бронирования пользователя /{}/", userId);
                break;
            case WAITING:
            case APPROVED:
            case REJECTED:
            case CANCELED:
                for (Booking booking : findAllByUserAndStatus(userId, bookingMapper.toStatus(state))) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                break;
            case FUTURE:
                for (Booking booking : findAllByUserFuture(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                break;
            case PAST:
                for (Booking booking : findAllByUserPast(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                break;
            case CURRENT:
                for (Booking booking : findAllByUserCurrent(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
        }

        return listDto;
    }


    private Collection<Booking> findAllByUserFuture(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ state=FUTURE", userId);
        return bookingRepository.findByBookerIdAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByUserPast(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ state=PAST", userId);
        return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByUserCurrent(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования пользователя /{}/ state=CURRENT", userId);
        return bookingRepository.getByUserCurrent(userId, LocalDateTime.now());
    }

    @Override
    public Collection<BookingDto> findAllByOwner(Integer userId, StatusDto state) {
        checkValidUser(userId);
        ArrayList<BookingDto> listDto = new ArrayList<>();
        switch (state) {
            case ALL:
                for (Booking booking : bookingRepository.getAllByOwner(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                log.info("запрошены бронирования вещей владельца /{}/", userId);
                break;
            case WAITING:
            case APPROVED:
            case REJECTED:
            case CANCELED:
                Status status = bookingMapper.toStatus(state);
                for (Booking booking : findAllByOwnerAndStatus(userId, status)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                break;
            case FUTURE:
                for (Booking booking : findAllByOwnerFuture(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                break;
            case PAST:
                for (Booking booking : findAllByOwnerPast(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
                break;
            case CURRENT:
                for (Booking booking : findAllByOwnerCurrent(userId)) {
                    Item item = itemService.getById(booking.getItem());
                    User user = userService.getById(booking.getBookerId());
                    listDto.add(bookingMapper.toDto(booking, item, user));
                }
        }
        return listDto;
    }

    private Collection<Booking> findAllByOwnerAndStatus(Integer userId, Status stat) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ со статусом /{}/", userId, stat);
        return bookingRepository.getAllByOwnerAndStatus(userId, stat);
    }

    private Collection<Booking> findAllByOwnerFuture(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ state=FUTURE", userId);
        return bookingRepository.getByOwnerFuture(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByOwnerPast(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ state=PAST", userId);
        return bookingRepository.getByOwnerPast(userId, LocalDateTime.now());
    }

    private Collection<Booking> findAllByOwnerCurrent(Integer userId) {
        checkValidUser(userId);
        log.info("запрошены бронирования вещей владельца /{}/ state=CURRENT", userId);
        return bookingRepository.getByOwnerCurrent(userId, LocalDateTime.now());
    }
}
