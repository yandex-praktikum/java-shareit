package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceJpaImpl implements BookingService {

    private final BookingJpaRepository bookingRepository;

    private final ItemJpaRepository itemRepository;

    private final UserJpaRepository userRepository;

    private final Validator validator;

    @Override
    public Booking createBooking(int userId, BookingDto bookingDto) {
        if ((validator.validateUser(userId, userRepository)) &&
                (validator.validateItem(bookingDto.getItemId(), itemRepository))) {
            if (validateBooking(bookingDto, userId)) {
                Booking booking = BookingMapper.bookingDtoToBooking(bookingDto);
                booking.setBooker(userRepository.findById(userId).get());
                booking.setItem(itemRepository.findById(bookingDto.getItemId()).get());
                booking.setStatus(Status.WAITING);
                return bookingRepository.save(booking);
            } else {
                log.info("Ошибка валидации бронирования");
                throw new ValidationException();
            }
        } else {
            throw new UserNotFoundException();
        }

    }

    @Override
    public Booking approvedBooking(int userId, int bookingId, boolean isApproved) {
        if (validator.validateBookingAndItem(bookingId, bookingRepository, itemRepository)) {
            Booking booking = bookingRepository.findById(bookingId).get();
            Item item = itemRepository.findById(booking.getItem().getId()).get();
            if (userId == item.getOwner().getId()) {
                if (booking.getStatus().equals(Status.WAITING)) {
                    if (isApproved) {
                        booking.setStatus(Status.APPROVED);
                    } else {
                        booking.setStatus(Status.REJECTED);
                    }
                    return bookingRepository.save(booking);
                } else {
                    log.info("Бронирование уже подтверждено");
                    throw new BookingApproveException();
                }
            } else {
                log.info("Ошибка доступа");
                throw new AccessErrorException();
            }
        } else {
            log.info("Бронирование или вещь не найдены");
            throw new BookingNotFoundException();
        }
    }

    @Override
    public Booking findBookingById(int userId, int bookingId) {
        if (validator.validateBookingAndItem(bookingId, bookingRepository, itemRepository)) {
            Booking booking = bookingRepository.findById(bookingId).get();
            Item item = itemRepository.findById(booking.getItem().getId()).get();
            if ((userId == booking.getBooker().getId()) ||
                    (userId == item.getOwner().getId())) {
                return booking;
            } else {
                log.info("Ошибка доступа");
                throw new AccessErrorException();
            }
        } else {
            log.info("Бронирование или вещь не найдены");
            throw new BookingNotFoundException();
        }
    }

    @Override
    public List<Booking> findBookingByUserId(int userId, String state,  int from, int size) {
        List<Booking> bookingsByUser = new ArrayList<>();
        if (userRepository.findById(userId).isPresent()) {
            if ((from >= 0) && (size > 0)) {
                Pageable page = PageRequest.of(0, size + from,
                        Sort.by(Sort.Direction.DESC, "end"));
                switch (state) {
                    case "ALL":
                        bookingsByUser = bookingRepository.findByBookerId(userId, page);
                        break;
                    case "CURRENT":
                        bookingsByUser = bookingRepository.findCurrentByBookerId(userId, page);
                        break;
                    case "PAST":
                        bookingsByUser = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                                page);
                        break;
                    case "FUTURE":
                        bookingsByUser = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                                page);
                        break;
                    case "WAITING":
                        bookingsByUser = bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING,
                                page);
                        break;
                    case "REJECTED":
                        bookingsByUser = bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED,
                                page);
                        break;
                    default:
                        log.info("Параметр state = " + state + " не существует");
                        throw new UnsupportedStatusException("Unknown state: " + state);
                }
                if (bookingsByUser.size() > size) {
                    return bookingsByUser.subList(from, bookingsByUser.size());
                } else {
                    return bookingsByUser;
                }
            } else {
                log.info("Параметры from и size не могут быть меньше 0");
                throw new ValidationException();
            }
        } else {
            log.info("Пользователь с id = " + userId + " не найден");
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<Booking> findBookingByOwnerId(int userId, String state,  int from, int size) {
        List<Booking> bookingsByOwner = new ArrayList<>();
        if (userRepository.findById(userId).isPresent()) {
            if ((from >= 0) && (size > 0)) {
                Pageable page = PageRequest.of(0, size + from,
                        Sort.by(Sort.Direction.DESC, "end"));
                switch (state) {
                    case "ALL":
                        bookingsByOwner = bookingRepository.findByOwnerId(userId, page);
                        break;
                    case "CURRENT":
                        bookingsByOwner = bookingRepository.findCurrentByOwnerId(userId, page);
                        break;
                    case "PAST":
                        bookingsByOwner = bookingRepository.findPastByOwnerId(userId, page);
                        break;
                    case "FUTURE":
                        bookingsByOwner = bookingRepository.findFutureByOwnerId(userId, page);
                        break;
                    case "WAITING":
                        bookingsByOwner = bookingRepository.findWaitingByOwnerId(userId, page);
                        break;
                    case "REJECTED":
                        bookingsByOwner = bookingRepository.findRejectedByOwnerId(userId, page);
                        break;
                    default:
                        log.info("Параметр state = " + state + " не существует");
                        throw new UnsupportedStatusException("Unknown state: " + state);
                }
                if (bookingsByOwner.size() > size) {
                    return bookingsByOwner.subList(from, bookingsByOwner.size());
                } else {
                    return bookingsByOwner;
                }
            } else {
                log.info("Параметры from и size не могут быть меньше 0");
                throw new ValidationException();
            }
        } else {
            log.info("Пользователь с id = " + userId + " не найден");
            throw new UserNotFoundException();
        }
    }

    private boolean validateBooking(BookingDto bookingDto, int userId) {
        boolean isValid = false;
        if ((bookingDto.getStart() != null) && (bookingDto.getEnd() != null)) {
            if ((bookingDto.getStart().isBefore(bookingDto.getEnd())) &&
                    (bookingDto.getStart().isAfter(LocalDateTime.now())) &&
                    (bookingDto.getEnd().isAfter(LocalDateTime.now())) &&
                    (!bookingDto.getEnd().equals(bookingDto.getStart()))) {
                isValid = true;
                Item item = itemRepository.findById(bookingDto.getItemId()).get();
                if (item.getOwner().getId() == userId) {
                    log.info("Владелец не может бронировать свою вещь");
                    throw new BookingNotFoundException();
                }
                if (!item.getAvailable()) {
                    log.info("Вещь не доступна для бронирования");
                    throw new ItemNotAvailableException();
                }
                if (!validateTimeOfBooking(bookingDto)) {
                   return false;
                }
            }
        }
        return isValid;
    }

    private boolean validateTimeOfBooking(BookingDto bookingDto) {
        List<Booking> bookingsByItemId = bookingRepository.findByItemId(bookingDto.getItemId());
        if (bookingsByItemId.isEmpty())  {
            return true;
        } else {
            for (Booking booking : bookingsByItemId) {
                if (((bookingDto.getEnd().isAfter(booking.getStart())) &&
                        (bookingDto.getEnd().isBefore(booking.getEnd())) &&
                        (bookingDto.getEnd().equals(booking.getEnd()))) ||
                        ((bookingDto.getStart().isAfter(booking.getStart())) &&
                                (bookingDto.getStart().isBefore(booking.getStart())) &&
                                (bookingDto.getStart().equals(booking.getStart())))) {
                    return false;
                }
            }
            return true;
        }
    }
}