package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.exception.BookingNotChangeStatusException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotAvalibleException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserIsNotOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingDtoToUser create(long userId, long itemId, BookingDto bookingDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("item not found"));
        if (!(item.getAvailable())) {
            throw new ItemNotAvalibleException("item not availible");
        }
        if (item.getOwner().getId() == userId) {
            throw new UserIsNotOwnerException("you can not booking this item");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found")));
        booking.setStatus(Status.WAITING);
        return BookingMapper.toBookingDtoToUser(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoToUser approveStatus(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("booking not found"));
        if (!(booking.getStatus().equals(Status.WAITING))) {
            throw new BookingNotChangeStatusException("status can not be change");
        }
        if (userId != booking.getItem().getOwner().getId()) {
            throw new UserIsNotOwnerException("user not owner this item and can not approve status");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDtoToUser(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoToUser getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("booking not found"));
        if (userId != booking.getItem().getOwner().getId() && userId != booking.getBooker().getId()) {
            throw new UserIsNotOwnerException("user not owner this item and can not get this booking");
        }
        return BookingMapper.toBookingDtoToUser(booking);
    }

    @Override
    public List<BookingDtoState> getBookingCurrentUser(long userId, State stateEnum, int from, int size) {
        if (from < 0) {
            throw new ItemRequestNotGoodParametrsException("from < 0");
        }
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        return bookingRepository.findAllByBooker(booker, PageRequest.of(from / size, size,
                        Sort.by(Sort.Direction.DESC, "start")))
                .stream()
                .map(BookingMapper::toBookingDtoState)
                .filter(bookingDtoState -> bookingDtoState.getStates().contains(stateEnum))
                .sorted(Comparator.comparing(BookingDtoState::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoState> getBookingCurrentOwner(long userId, State stateEnum, int from, int size) {
        if (from < 0) {
            throw new ItemRequestNotGoodParametrsException("from < 0");
        }
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        return bookingRepository.findAllByItemOwner(owner, PageRequest.of(from / size, size,
                        Sort.by(Sort.Direction.DESC, "start")))
                .stream()
                .map(BookingMapper::toBookingDtoState)
                .filter(bookingDtoState -> bookingDtoState.getStates().contains(stateEnum))
                .sorted(Comparator.comparing(BookingDtoState::getStart).reversed())
                .collect(Collectors.toList());
    }
}
