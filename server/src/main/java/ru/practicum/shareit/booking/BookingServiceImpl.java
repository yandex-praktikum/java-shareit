package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto, Long bookerId) {
        checkUserExists(bookerId);

        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }


        ItemDto itemDto = itemService.getItemById(bookingDto.getItemId(), bookerId);
        if (!itemDto.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }


        List<ItemDto> ownerItems = itemService.getItemsByOwner(bookerId);
        boolean isOwnItem = ownerItems.stream()
                .anyMatch(item -> item.getId().equals(bookingDto.getItemId()));
        if (isOwnItem) {
            throw new EntityNotFoundException("Нельзя бронировать свою собственную вещь");
        }

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBookerId(bookerId);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));

        return BookingMapper.toBookingDtoWithDetails(savedBooking, booker, item);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long bookingId, Long ownerId, boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с ID " + bookingId + " не найдено"));

        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));

        if (!item.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("Доступ запрещен:только владелец вещи может подтверждать бронирование");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Бронирование уже обработано");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);

        User booker = userRepository.findById(updatedBooking.getBookerId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return BookingMapper.toBookingDtoWithDetails(updatedBooking, booker, item);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с ID " + bookingId + " не найдено"));


        List<ItemDto> ownerItems = itemService.getItemsByOwner(userId);
        boolean isOwner = ownerItems.stream()
                .anyMatch(item -> item.getId().equals(booking.getItemId()));
        boolean isBooker = booking.getBookerId().equals(userId);

        if (!isOwner && !isBooker) {
            throw new AccessDeniedException("Доступ к бронированию запрещен");
        }

        User booker = userRepository.findById(booking.getBookerId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));

        return BookingMapper.toBookingDtoWithDetails(booking, booker, item);
    }

    @Override
    public BookingDto cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование с ID " + bookingId + " не найдено"));

        if (!booking.getBookerId().equals(userId)) {
            throw new EntityNotFoundException("Только автор бронирования может отменить его");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Можно отменить только бронирование в статусе WAITING");
        }

        booking.setStatus(BookingStatus.CANCELED);
        Booking updatedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Long bookerId, String state) {
        checkUserExists(bookerId);
        List<Booking> userBookings = bookingRepository.findByBookerId(bookerId).stream()
                .filter(booking -> booking.getBookerId().equals(bookerId))
                .sorted((b1, b2) -> b2.getStart().compareTo(b1.getStart()))
                .collect(Collectors.toList());

        return filterBookingsByStateWithDetails(userBookings, state);
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long ownerId, String state) {
        checkUserExists(ownerId);


        List<ItemDto> ownerItems = itemService.getItemsByOwner(ownerId);
        List<Long> ownerItemIds = ownerItems.stream()
                .map(ItemDto::getId)
                .collect(Collectors.toList());

        List<Booking> ownerBookings = bookingRepository.findByItemIdIn(ownerItemIds).stream()
                .filter(booking -> ownerItemIds.contains(booking.getItemId()))
                .sorted((b1, b2) -> b2.getStart().compareTo(b1.getStart()))
                .collect(Collectors.toList());

        return filterBookingsByStateWithDetails(ownerBookings, state);
    }

    private List<BookingDto> filterBookingsByStateWithDetails(List<Booking> bookings, String state) {
        if (bookings.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, User> usersMap = userRepository.findAllById(
                bookings.stream().map(Booking::getBookerId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(User::getId, user -> user));

        Map<Long, Item> itemsMap = itemRepository.findAllById(
                bookings.stream().map(Booking::getItemId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(Item::getId, item -> item));

        LocalDateTime now = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "ALL":
                return bookings.stream()
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            case "CURRENT":
                return bookings.stream()
                        .filter(booking -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now))
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            case "PAST":
                return bookings.stream()
                        .filter(booking -> booking.getEnd().isBefore(now))
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            case "FUTURE":
                return bookings.stream()
                        .filter(booking -> booking.getStart().isAfter(now))
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            case "WAITING":
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.WAITING)
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            case "REJECTED":
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.REJECTED)
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            case "CANCELED":
                return bookings.stream()
                        .filter(booking -> booking.getStatus() == BookingStatus.CANCELED)
                        .map(booking -> BookingMapper.toBookingDtoWithDetails(
                                booking,
                                usersMap.get(booking.getBookerId()),
                                itemsMap.get(booking.getItemId())))
                        .collect(Collectors.toList());

            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }
}

