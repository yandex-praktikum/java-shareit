package ru.practicum.shareit.item.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingShortMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(Long ownerId, @Valid ItemCreateDto itemDto) {

        log.info("Создание вещи пользователем {}", ownerId);

        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Запрос с id = " + itemDto.getRequestId() + " не найден"));

            item.setRequest(request);
        }

        log.info("Вещь {} успешно создана", item.getId());

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {

        log.info("Обновление вещи {} пользователем {}", itemId, ownerId);

        Item existingItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь не найдена")
        );

        if (!(existingItem.getOwner().getId().equals(ownerId))) {
            throw new AccessException("Только владелец может обновлять свои вещи");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        log.info("Вещь {} успешно обновлена", itemId);

        return ItemMapper.toItemDto(itemRepository.save(existingItem));
    }

    @Override
    public ItemDto getById(Long itemId, Long userId) {

        log.info("Получение вещи {} пользователем {}", itemId, userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Вещь не найдена"));

        List<CommentDto> comments = commentRepository.findByItemIdOrderByCreatedAsc(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());

        BookingShortDto lastBooking = null;
        BookingShortDto nextBooking = null;

        if (item.getOwner().getId().equals(userId)) {

            Booking last = bookingRepository
                    .findFirstByItemIdAndStartBeforeOrderByStartDesc(
                            itemId,
                            LocalDateTime.now())
                    .orElse(null);

            Booking next = bookingRepository
                    .findFirstByItemIdAndStartAfterOrderByStartAsc(
                            itemId,
                            LocalDateTime.now())
                    .orElse(null);

            lastBooking = BookingShortMapper.toDto(last);
            nextBooking = BookingShortMapper.toDto(next);
        }

        return ItemMapper.toItemDto(
                item,
                lastBooking,
                nextBooking,
                comments
        );
    }

    @Override
    public Collection<ItemDto> getItemsByOwner(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> search(String text) {

        log.info("Поиск вещей по запросу '{}'", text);

        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {

        log.info("Добавление комментария к вещи {} пользователем {}", itemId, userId);

        User author = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new NotFoundException("Вещь не найдена")
                );

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

        boolean hasBooking =
                bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndBefore(
                        userId,
                        itemId,
                        BookingStatus.APPROVED,
                        now
                );

        log.info(
                "COMMENT CHECK: userId={}, itemId={}, now={}, hasBooking={}",
                userId,
                itemId,
                now,
                hasBooking
        );

        if (!hasBooking) {
            throw new ValidationException(
                    "Оставить комментарий может только пользователь, который арендовал вещь");
        }

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);

        Comment saved = commentRepository.save(comment);

        log.info("Комментарий {} успешно добавлен", saved.getId());

        return CommentMapper.toDto(saved);
    }
}
