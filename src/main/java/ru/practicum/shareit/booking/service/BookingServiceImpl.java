package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.IncorrectBookingParameterException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Date;
import java.util.Optional;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto getBooking(Integer bookerId, Integer id) {
        Optional<Booking> bookingOption = bookingRepository.findById(id);
        if (bookingOption.isPresent()) {
            Booking booking = bookingOption.get();
            if (booking.getBooker().getId().equals(bookerId) || booking.getItem().getOwner().getId().equals(bookerId)) {
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new BookingNotFoundException("Неверные параметры");
            }
        } else {
            throw new BookingNotFoundException("Брони с таким id нет");
        }
    }

    @Override
    public BookingDto booking(Integer bookerId, BookingDto bookingDto) {
        checkDates(bookingDto);

        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isPresent() && item.get().getIsAvailable()) {
            Integer ownerId = item.get().getOwner().getId();
            if (ownerId.equals(bookerId)) {
                throw new IncorrectParameterException("Неверные параметры");
            }

            Booking booking = new Booking();
            Optional<User> user = userRepository.findById(bookerId);
            if (user.isPresent()) {
                booking.setBooker(user.get());
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
            booking.setDateBegin(bookingDto.getStart());
            booking.setDateEnd(bookingDto.getEnd());
            booking.setItem(item.get());
            booking.setStatus("WAITING");

            bookingRepository.save(booking);

            return BookingMapper.toBookingDto(booking);

        } else if (!item.isPresent()) {
            throw new IncorrectParameterException("Вещи с таким id нет");
        } else {
            throw new IncorrectBookingParameterException("Вещь недоступна");
        }
    }

    @Override
    public BookingDto aprove(Integer ownerId, Integer bookingId, boolean approved) {
        Optional<Booking> bookingOption = bookingRepository.findById(bookingId);
        if (bookingOption.isPresent()) {
            Item item = bookingOption.get().getItem();
            User owner = item.getOwner();
            if (owner.getId().equals(ownerId)) {
                Booking booking = bookingOption.get();
                if (booking.getStatus().equals("APPROVED")) {
                    throw new IncorrectBookingParameterException("Неверные параметры");
                }
                String status;
                if (approved) {
                    status = "APPROVED";
                } else {
                    status = "REJECTED";
                }
                booking.setStatus(status);

                bookingRepository.save(booking);
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new BookingNotFoundException("Неверные параметры");
            }
        } else {
            throw new BookingNotFoundException("Брони с такой ID нет");
        }
    }

    private void checkDates(BookingDto bookingDto) {
        if (bookingDto.getEnd() == null || bookingDto.getStart() == null
                || bookingDto.getEnd().equals(bookingDto.getStart())
                || bookingDto.getEnd().before(bookingDto.getStart())
                || bookingDto.getEnd().before(new Date())
                || bookingDto.getStart().before(new Date())
        ) {
            throw new IncorrectBookingParameterException("Неверные параметры");
        }
    }

    @Override
    public List<BookingDto> getBooking(BookingStatus state, Integer bookerId, Integer from, Integer size) {
        Optional<User> user = userRepository.findById(bookerId);
        if (user.isPresent()) {
            List<Booking> bookingList = new ArrayList<>();
            if (from == null && size == null) {
                bookingList = bookingRepository.findByBooker(user.get());
            } else if (from >= 0 && size > 0) {
                bookingList = bookingRepository.findByBookerByPage(user.get().getId(), from, size);
            } else {
                throw new IncorrectBookingParameterException("Неверные параметры");
            }

            List<Booking> list = getBookingListByStatus(state, bookingList);
            return BookingMapper.toBookingDtoList(list);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    private List<Booking> getBookingListByStatus(BookingStatus state, List<Booking> bookingList) {
        List<Booking> list = new ArrayList<>();
        switch (state) {
            case PAST:
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().before(new Date()))
                        .filter(booking -> booking.getDateEnd().before(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().after(new Date()) && booking.getDateEnd().after(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                list = bookingList.stream()
                        .filter(booking -> booking.getDateBegin().before(new Date()))
                        .filter(booking -> booking.getDateEnd().after(new Date()))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                break;
            case WAITING:
                list = bookingList.stream()
                        .filter(booking -> booking.getStatus().equals("WAITING"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                list = bookingList.stream()
                        .filter(booking -> booking.getStatus().equals("REJECTED"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                break;
            case ALL:
                list = bookingList.stream()
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                break;
        }
        return list;
    }

    @Override
    public List<BookingDto> ownerItemsBookingLists(BookingStatus state, Integer ownerId, Integer from, Integer size) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isPresent()) {
            List<Item> ownerItemList = itemRepository.findByOwner(user.get());
            List<Booking> bookingList = new ArrayList<>();
            ownerItemList.forEach(item -> {
                        List<Booking> itemBookingList = new ArrayList<>();
                        if (from == null && size == null) {
                            itemBookingList = bookingRepository.findByItem(item);
                        } else if (from >= 0 && size > 0) {
                            itemBookingList = bookingRepository.findByItemByLimits(item.getId(), from, size);
                        } else {
                            throw new IncorrectBookingParameterException("Неверные параметры");
                        }
                        bookingList.addAll(itemBookingList);
                    }
            );
            List<Booking> list = getBookingListByStatus(state, bookingList);
            return BookingMapper.toBookingDtoList(list);
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
