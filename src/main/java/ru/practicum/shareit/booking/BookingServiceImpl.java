package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingStatusDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.mopel.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;

    @Override
    public BookingStatusDto getById(Long userId, Long bookingId) {
        Booking booking = findBookingById(bookingId);

        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();

        if (bookerId.equals(userId) || ownerId.equals(userId)) {
            return bookingMapper.toBookingDto(booking);
        } else {
            log.error("Only the item owner or the booker can get the booking");
            throw new NotFoundException("Only the item owner or the booker can get the booking");
        }
    }

    @Override
    public BookingStatusDto create(Long bookerId, BookingDto bookingDto) {
        User booker = userRepository.findById(bookerId)
                                    .orElseThrow(() -> {
                                           log.error("User with id " + bookerId + " not found!");
                                           return new NotFoundException(
                                                   "User with id " + bookerId + " not found!"
                                           );
                                       });

        Long itemId = bookingDto.getItemId();
        Item item = itemRepository.findById(itemId)
                                  .orElseThrow(() -> {
                                         log.error("Item with id {} not found", itemId);
                                         return new NotFoundException(
                                                 "Item not found with id: " + itemId
                                         );
                                     });

        if (bookerId.equals(item.getOwner().getId())) {
            log.error("User can't booking own item");
            throw new NotFoundException("User can't booking own item");
        }

        if (item.getAvailable().equals(false)) {
            throw new BadRequestException("The item is not available!");
        }
        Booking booking = bookingMapper.toBooking(bookingDto, booker, item, Status.WAITING);

        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingStatusDto approve(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = findBookingById(bookingId);
        if (booking.getStatus().equals(Status.APPROVED)) {
            log.error("Booking status can't change after approve");
            throw new BadRequestException("Booking status can't change after approve");
        }

        Long itemOwnerId = booking.getItem().getOwner().getId();

        if (!itemOwnerId.equals(ownerId)) {
            log.error("User with id {} tries to approve not own item", itemOwnerId);
            throw new NotFoundException("Item has not been added user id " + itemOwnerId);
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingStatusDto> getByBookerId(Long bookerId, String stateString) {
        State state;
        try {
            state = State.valueOf(stateString.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(stateString);
            throw new BadRequestException("Unknown state: " + stateString);
        }

        List<Booking> bookings = List.of();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker(bookerId);
                if (bookings.isEmpty()) {
                    log.error("No bookings for booker with id {}", bookerId);
                    throw new NotFoundException("No bookings for booker with id " + bookerId);
                }
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerAndPastState(bookerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerAndFutureState(bookerId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerAndCurrentState(bookerId);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, Status.REJECTED);
                break;
        }

        return bookings.stream()
                       .map(bookingMapper::toBookingDto)
                       .collect(Collectors.toList());
    }

    @Override
    public List<BookingStatusDto> getByOwnerId(Long ownerId, String stateString) {
        State state;
        try {
            state = State.valueOf(stateString.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(stateString);
            throw new BadRequestException("Unknown state: " + stateString);
        }

        List<Booking> bookings = List.of();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByOwner(ownerId);
                if (bookings.isEmpty()) {
                    log.error("No bookings for owner with id {}", ownerId);
                    throw new NotFoundException("No bookings for owner with id " + ownerId);
                }
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerAndPastState(ownerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerAndFutureState(ownerId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerAndCurrentState(ownerId);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, Status.REJECTED);
                break;
        }

        return bookings.stream()
                       .map(bookingMapper::toBookingDto)
                       .collect(Collectors.toList());
    }

    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                                .orElseThrow(() -> {
                                    log.error(
                                            "Booking with id {} not found", bookingId
                                    );
                                    return new NotFoundException(
                                            "Booking not found with id: " + bookingId
                                    );
                                });
    }

}
