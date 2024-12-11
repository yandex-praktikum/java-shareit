package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j(topic = "ItemServiceImpl")
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final CommentRepository commentRepo;
    private final BookingRepository bookingRepo;
    private final ItemRequestRepository itemRequestRepo;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public ItemDto addItem(int userId, ItemSaveDto itemSaveDto) {
        log.info("Добавление предмета пользователем {}, Предмет - {}", userId, itemSaveDto);
        User owner = getUserById(userId);
        Item item = itemMapper.map(itemSaveDto);
        item.setOwner(owner);
        Integer requestId = itemSaveDto.getRequestId();
        if (requestId != null) {
            ItemRequest itemRequest = itemRequestRepo.findById(requestId)
                    .orElseThrow(() -> new ItemNotFoundException("Запрос с id " + requestId + " не найден."));
            item.setRequest(itemRequest);
        }
        Item savedItem = itemRepo.save(item);
        log.info("Добавили предмет пользователем {}, Предмет - {}", userId, savedItem);
        return itemMapper.map(savedItem);
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentSaveDto commentSaveDto) {
        User owner = getUserById(userId);
        Item item = getItemById(itemId);
        Booking booking = bookingRepo
                .findByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new NotValidException(Comment.class, "cannot be posted. Please check user id " +
                        userId + " has previously booked item id " + itemId));

        Comment comment = commentMapper.map(commentSaveDto);
        comment.setAuthor(booking.getBooker());
        comment.setItem(booking.getItem());
        Comment savedComment = commentRepo.save(comment);

        return commentMapper.map(savedComment);
    }

    @Transactional
    @Override
    public ItemDto updateItem(int userId, int itemId, ItemSaveDto itemSaveDto) {
        Item item = getItemById(itemId);
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenException("item could be updated only by owner");
        }
        itemMapper.updateItemFromDto(item, itemSaveDto);
        Item savedItem = itemRepo.save(item);

        return itemMapper.map(savedItem);
    }

    @Override
    public ItemDto getItem(int itemId) {
        Item item = getItemById(itemId);
        ItemDto itemDto = itemMapper.map(item);
        Collection<LocalDateTime> futureBookings =
                bookingRepo.findAllByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now()).stream()
                        .map((Booking::getStart))
                        .toList();

        LocalDateTime nextBooking = findNextBooking(futureBookings);
        itemDto.setNextBooking(nextBooking);
        LocalDateTime lastBooking = findLastBooking(futureBookings);
        itemDto.setLastBooking(lastBooking);
        Collection<CommentDto> commentsDto = commentMapper.map(commentRepo.findAllByItemId(itemId));
        itemDto.setComments(commentsDto);

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAllOwnerItems(int userId) {
        Map<Integer, ItemDto> itemIdToItemDto = itemRepo
                .findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::map)
                .collect(Collectors.toMap(ItemDto::getId, Function.identity()));

        Map<Integer, List<Booking>> itemIdToFutureBookings = bookingRepo
                .findAllByItemIdInAndStartAfterOrderByStartAsc(itemIdToItemDto.keySet(), LocalDateTime.now())
                .stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId(), Collectors.toList()));

        Map<Integer, List<Comment>> itemIdToComments = commentRepo
                .findAllByItemIdIn(itemIdToItemDto.keySet()).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(), Collectors.toList()));

        for (ItemDto itemDto : itemIdToItemDto.values()) {
            if (!itemIdToFutureBookings.containsKey(itemDto.getId())) {
                continue;
            }
            Collection<LocalDateTime> futureItemBookings = itemIdToFutureBookings.get(itemDto.getId()).stream()
                    .map(Booking::getStart)
                    .toList();
            LocalDateTime nextBooking = findNextBooking(futureItemBookings);
            itemDto.setNextBooking(nextBooking);
            LocalDateTime lastBooking = findLastBooking(futureItemBookings);
            itemDto.setLastBooking(lastBooking);

            if (!itemIdToComments.containsKey(itemDto.getId())) {
                continue;
            }
            List<Comment> comments = itemIdToComments.get(itemDto.getId());
            itemDto.setComments(commentMapper.map(comments));
        }

        return itemIdToItemDto.values();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepo.searchItems(text);
        return itemMapper.map(items);
    }

    private LocalDateTime findNextBooking(Collection<LocalDateTime> futureBookings) {
        return futureBookings.stream()
                .reduce((first, last) -> first)
                .orElse(null);
    }

    private LocalDateTime findLastBooking(Collection<LocalDateTime> futureBookings) {
        return futureBookings.stream()
                .reduce((first, last) -> last)
                .orElse(null);
    }

    private User getUserById(int userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    private Item getItemById(int itemId) {
        return itemRepo.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException("Предмет с id " + itemId + " не найден"));
    }
}
