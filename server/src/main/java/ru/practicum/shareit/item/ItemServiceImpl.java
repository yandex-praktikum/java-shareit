package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long userId) {
        log.info("Добавление вещи");
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        Item item;
        if (itemDto.getRequestId() != null) {
            ItemRequest request = requestRepository.findById(itemDto.getRequestId()).get();
            item = ItemMapper.fromItemDto(itemDto, true, userId, request);
        } else {
            item = ItemMapper.fromItemDto(itemDto, true, userId, null);
        }
        itemRepository.save(item);
        return ItemMapper.toFullItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(ItemUpdateDto itemDto, Long userId, Long itemId) {
        log.info("Обновление вещи");
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        List<Item> items = itemRepository.findAll();
        Optional<Item> itemO = itemRepository.findById(itemId);
        Item item;
        if (itemDto.getAvailable() != null) {
            if (itemDto.getAvailable().equals("true")) {
                item = ItemMapper.fromItemUpdateDto(itemDto, true, userId, itemId);
            } else {
                item = ItemMapper.fromItemUpdateDto(itemDto, false, userId, itemId);
            }
        } else {
            item = ItemMapper.fromItemUpdateDto(itemDto, itemO.orElseThrow().isAvailable(), userId, itemId);
        }
        if (userId.equals(itemO.orElseThrow().getOwnerId())) {
            isValidateItem(item, itemO.orElseThrow());
        }
        return ItemMapper
                .toFullItemDto(itemRepository
                        .saveAndFlush(itemO.orElseThrow()));
    }

    private void isValidateItem(Item item, Item itemToChange) {
        if (item.getName() != null) {
            itemToChange.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemToChange.setDescription(item.getDescription());
        }
        if (item.isAvailable()) {
            itemToChange.setAvailable(true);
        }
        if (!item.isAvailable()) {
            itemToChange.setAvailable(false);
        }
    }

    @Transactional
    @Override
    public ItemDtoWithBookingDates findItemById(Long id, Long userId) {
        log.info("Поиск вещи по id");
        List<Item> items = itemRepository.findAll();
        Optional<Item> itemO = itemRepository.findById(id);
        if (itemO.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        } else {
            List<Booking> bookingsAllStatus = bookingRepository.findAllByItemIdOrderByStartDesc(id);
            List<Booking> bookings = new ArrayList<>();
            for (Booking allStatus : bookingsAllStatus) {
                if (!allStatus.getStatus().equals(BookingStatus.REJECTED)) {
                    bookings.add(allStatus);
                }
            }
            Optional<Booking> lastBooking = Optional.empty();
            Optional<Booking> nextBooking = Optional.empty();
            Long itemId = itemO.get().getId();
            List<Comment> comments = commentRepository.findAll();
            List<Comment> comment = new ArrayList<>();
            if (!comments.isEmpty()) {
                comment = commentRepository.findAllByItemId(itemO.get().getId());
            }
            for (Booking booking : bookings) {
                if (booking.getStart().isBefore(LocalDateTime.now())) {
                    if (lastBooking.isPresent()) {
                        if (booking.getStart().isAfter(lastBooking.get().getStart())) {
                            lastBooking = Optional.of(booking);
                        }
                    } else {
                        lastBooking = Optional.of(booking);
                    }
                } else {
                    if (nextBooking.isPresent()) {
                        if (booking.getStart().isBefore(nextBooking.get().getStart())) {
                            nextBooking = Optional.of(booking);
                        }
                    } else {
                        nextBooking = Optional.of(booking);
                    }
                }
            }
            if ((lastBooking.isEmpty()) && (nextBooking.isEmpty()) ||
                    (!itemO.get().getOwnerId().equals(userId))) {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        null,
                        null,
                        CommentMapper.toCommentsFullDto(comment)
                );
            } else if (lastBooking.isEmpty()) {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        null,
                        BookingMapper.toBookingToItemDto(nextBooking.get()),
                        CommentMapper.toCommentsFullDto(comment)
                );
            } else if (nextBooking.isEmpty()) {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        BookingMapper.toBookingToItemDto(lastBooking.get()),
                        null,
                        CommentMapper.toCommentsFullDto(comment)
                );
            } else {
                return ItemMapper.toItemDtoWithBookingDates(
                        itemO.get(),
                        BookingMapper.toBookingToItemDto(lastBooking.get()),
                        BookingMapper.toBookingToItemDto(nextBooking.get()),
                        CommentMapper.toCommentsFullDto(comment)
                );
            }
        }
    }

    @Override
    public List<ItemDto> getAll() {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : itemRepository.findAll()) {
            itemsDto.add(ItemMapper.toFullItemDto(item));
        }
        return itemsDto;
    }

    @Transactional
    @Override
    public List<ItemDtoWithBookingDates> getAllByUserId(Long userId, Long from, Long size) {
        List<Item> itemsToCheck = new ArrayList<>();
        if ((from == null) || (size == null)) {
            itemsToCheck = itemRepository.findAll();
        } else if (((from == 0) && (size == 0)) || (size <= 0)) {
            throw new ValidationException("");
        //} else if (from == 0) {
          //  return new ArrayList<>();
        } else {
            Page<Item> itemsByPage = itemRepository.findAll(PageRequest.of(from.intValue(), size.intValue()));
            itemsToCheck = itemsByPage.toList();
        }
        List<ItemDtoWithBookingDates> itemsByUser = new ArrayList<>();
        for (Item item : itemsToCheck) {
            if (item.getOwnerId().equals(userId)) {
                List<Booking> bookings = bookingRepository.findAllByItemIdOrderByStartDesc(item.getId());
                Optional<Booking> lastBooking = Optional.empty();
                Optional<Booking> nextBooking = Optional.empty();
                List<Comment> comments = commentRepository.findAll();
                List<Comment> comment = new ArrayList<>();
                if (!comments.isEmpty()) {
                    comment = commentRepository.findAllByItemId(item.getId());
                }
                LocalDateTime createdTime = LocalDateTime.now();
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(createdTime)) {
                        if (lastBooking.isPresent()) {
                            if (booking.getEnd().isAfter(lastBooking.get().getEnd())) {
                                lastBooking = Optional.of(booking);
                            }
                        } else {
                            lastBooking = Optional.of(booking);
                        }
                    } else {
                        if (nextBooking.isPresent()) {
                            if (booking.getEnd().isBefore(nextBooking.get().getEnd())) {
                                nextBooking = Optional.of(booking);
                            }
                        } else {
                            nextBooking = Optional.of(booking);
                        }
                    }
                }
                if (((lastBooking.isEmpty()) && (nextBooking.isEmpty())) ||
                        (!item.getOwnerId().equals(userId))) {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            null,
                            null,
                            CommentMapper.toCommentsFullDto(comment)
                    ));
                } else if (lastBooking.isEmpty()) {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            null,
                            BookingMapper.toBookingToItemDto(nextBooking.get()),
                            CommentMapper.toCommentsFullDto(comment)
                    ));
                } else if (nextBooking.isEmpty()) {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            BookingMapper.toBookingToItemDto(lastBooking.get()),
                            null,
                            CommentMapper.toCommentsFullDto(comment)
                    ));
                } else {
                    itemsByUser.add(ItemMapper.toItemDtoWithBookingDates(
                            item,
                            BookingMapper.toBookingToItemDto(lastBooking.get()),
                            BookingMapper.toBookingToItemDto(nextBooking.get()),
                            CommentMapper.toCommentsFullDto(comment)
                    ));
                }
            }
        }

        return itemsByUser;
    }

    @Transactional
    @Override
    public List<ItemDto> search(String text, Long from, Long size) {
        log.info("Поиск вещи");
        List<ItemDto> checked = new ArrayList<>();

        if (!text.isBlank()) {
            for (Item item : itemRepository.findItemsByQuery(text)) {
                if (item.isAvailable()) {
                    checked.add(ItemMapper.toFullItemDto(item));
                }
            }
        }
        if ((from == null) || (size == null)) {
            return checked;
        } else if (((from == 0) && (size == 0)) || (size <= 0)) {
            throw new ValidationException("");
        }
        return checked;
    }

    @Transactional
    @Override
    public CommentFullDto createComment(CommentDto commentSto, Long itemId, Long userId) {
        List<Item> items = itemRepository.findAll();
        Optional<Item> item = itemRepository.findById(itemId);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        List<User> user = userRepository.findAll();
        LocalDateTime createdTime = LocalDateTime.now();
        if (bookings.isEmpty()) {
            throw new NotFoundException("У вас нет ни одного бронирования");
        }
        for (Booking booking : bookings) {
            if ((booking.getItem().getId().equals(itemId)) &&
                    (!booking.getStatus().equals(BookingStatus.REJECTED)) &&
                    (booking.getEnd().isBefore(createdTime))) {
                return CommentMapper.toCommentFullDto(commentRepository.save(
                        CommentMapper.fromCommentDto(
                                commentSto,
                                item.get(),
                                userRepository.findById(userId).get(),
                                LocalDateTime.now())));
            }
        }
        throw new ValidationException("У вас не было подтвержденного бронирования вещи с id :" + itemId);
    }
}