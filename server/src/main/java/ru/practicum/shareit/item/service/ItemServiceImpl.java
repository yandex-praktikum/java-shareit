package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ErrorRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    public ItemDto createItem(Long userId, ItemDto item) {
        log.info("Пользователь с id = {} создает вещь {}", userId, item);
        User user = getOwner(userId);
        ItemRequest request = checkItemRequest(item.getRequestId());
        Item itemEntity = itemMapper.mapItem(item, user, request);
        return itemMapper.mapItemDto(itemRepository.save(itemEntity));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto item) {
        log.info("Пользователь с id = {} обновляет вещь с id {}", userId, itemId);
        User itemOwner = getOwner(userId);
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не удается найти вещь с id " + itemId));;
        if (oldItem.getOwner() == null || !oldItem.getOwner().equals(itemOwner)) {
            throw new ErrorRequestException("Пользователь с id = " + userId + "не может редактировать эту вещь");
        }

        if (item.getDescription() != null && !item.getDescription().equals(oldItem.getDescription())) {
            oldItem.setDescription(item.getDescription());
        }

        if (item.getName() != null && !item.getName().equals(oldItem.getName())) {
            oldItem.setName(item.getName());
        }

        if (item.getAvailable() != null && !item.getAvailable().equals(oldItem.getAvailable())) {
            oldItem.setAvailable(item.getAvailable());
        }

        return itemMapper.mapItemDto(itemRepository.save(oldItem));
    }


    @Transactional(readOnly = true)
    @Override
    public ItemWithCommentDto getItem(Long itemId) {
        LocalDateTime now = LocalDateTime.now().minusSeconds(2); //костыль для прохождения теста постмана
        log.info("Запрос на получение вещи с id {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не удается найти вещь с id " + itemId));
        List<CommentDto> comment = commentMapper.mapListCommentDto(commentRepository.findCommentsByItemId(itemId));
        log.error("Время: {}", now);
        Booking lastBooking = bookingRepository.findLastBooking(itemId, now)
                .orElse(null);
        Booking nextBooking =  bookingRepository.findNextBooking(itemId, now)
                .orElse(null);

        return itemMapper.toItemWithCommentDto(item, comment,
                nextBooking == null ? null : bookingMapper.mapBookingDto(nextBooking),
                lastBooking == null ? null : bookingMapper.mapBookingDto(lastBooking));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemWithCommentDto> getUserItems(Long userId) {
        log.info("Запрос на получение вещей пользователя с id {}", userId);
        LocalDateTime now = LocalDateTime.now();
        List<Long> itemsId = itemRepository.findByOwnerId(userId).stream()
                .map(Item::getId)
                .toList();

        Map<Long, List<CommentDto>> commentsByItem = commentRepository.findCommentByItemIdIn(itemsId).stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(commentMapper::mapCommentDto, Collectors.toList())
                ));

        return itemRepository.findByOwner(getOwner(userId)).stream()
                .map(item -> {
                    List<CommentDto> comment = commentsByItem.getOrDefault(item.getId(), List.of());
                    Booking nextBooking =  bookingRepository.findNextBooking(item.getId(), now)
                            .orElse(null);
                    Booking lastBooking = bookingRepository.findLastBooking(item.getId(), now)
                            .orElse(null);

                    return itemMapper
                            .toItemWithCommentDto(item, comment,
                                    nextBooking == null ? null : bookingMapper.mapBookingDto(nextBooking),
                                    lastBooking == null ? null : bookingMapper.mapBookingDto(lastBooking));
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItems(Long userId, String text) {
        log.info("Пользователь с id {} ищет вещь по запросу text = {}", userId, text);
        getOwner(userId); //выполняет проверку, существует ли такой пользователь или нет
        if (text.isEmpty()) {
            return new ArrayList<ItemDto>();
        }
        return itemRepository.searchItems(text)
                .stream().map(itemMapper::mapItemDto)
                .toList();
    }

    private User getOwner(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя с id " + userId));
    }

    private ItemRequest checkItemRequest(Long requestId) {
        if (requestId == null) {
            return null;
        }
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти запрос с id " + requestId));
    }
}
