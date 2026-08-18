package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto create(Long userId, BookingRequestDto requestDto) {

        log.info("Создание бронирования пользовтелем {} для вещи  {}", userId, requestDto.getItemId());

        User booker = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Вещь с id = " + requestDto.getItemId() + " не найдена"));

        if (!item.getAvailable()) {
            throw new ParameterNotValidException("Вещь недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new AccessException("Нельзя бронировать собственную вещь");
        }

        Booking booking = Booking.builder()
                .start(requestDto.getStart())
                .end(requestDto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        log.info("Бронирование {} успешно создано", savedBooking.getId());

        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto approve(Long ownerId, Long bookingId, Boolean approved) {

        log.info("Пользователь {} изменяет статус бронирования для вещи {}", ownerId, bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new AccessException("Подтверждать бронирование может только владелец вещи");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ParameterNotValidException("Бронирование уже обработано");
        }

        booking.setStatus(
                approved
                        ? BookingStatus.APPROVED
                        : BookingStatus.REJECTED
        );

        log.info("Статус бронирования вещи {} изменен на {}", booking.getId(), booking.getStatus());

        return BookingMapper.toBookingDto(
                bookingRepository.save(booking)
        );
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {

        log.info("Получение бронирования {} пользователем {}",
                bookingId,
                userId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException("Бронирование не найдено"));

        boolean isBooker =
                booking.getBooker().getId().equals(userId);

        boolean isOwner =
                booking.getItem().getOwner().getId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new AccessException("Недостаточно прав");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Long userId, BookingState state) {

        log.info("Получение списка бронирований пользователя {}, состояние {}",
                userId,
                state);

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с id = " + userId + " не найден"));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(userId);

            case CURRENT -> bookingRepository.findCurrentByBooker(userId, now);

            case PAST -> bookingRepository.findPastByBooker(userId, now);

            case FUTURE -> bookingRepository.findFutureByBooker(userId, now);

            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(
                    userId,
                    BookingStatus.WAITING);

            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(
                    userId,
                    BookingStatus.REJECTED);
        };

        return BookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long ownerId, BookingState state) {

        log.info("Получение списка бронирований владельца {}, состояние {}",
                ownerId,
                state);

        userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с id = " + ownerId + " не найден"));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {

            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);

            case CURRENT -> bookingRepository.findCurrentByOwner(ownerId, now);

            case PAST -> bookingRepository.findPastByOwner(ownerId, now);

            case FUTURE -> bookingRepository.findFutureByOwner(ownerId, now);

            case WAITING -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                    ownerId,
                    BookingStatus.WAITING);

            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(
                    ownerId,
                    BookingStatus.REJECTED);
        };

        return BookingMapper.toBookingDtoList(bookings);
    }


}