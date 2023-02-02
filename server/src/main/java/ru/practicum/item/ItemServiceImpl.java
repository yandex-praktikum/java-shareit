package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.exception.CommentAddException;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.exception.NoRightsToUpdateException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Primary
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDtoResponse getItemById(Long itemId, Long ownerId) {
        log.info("item with id " + itemId + " returned");
        return createItemForResponse(itemId, ownerId);
    }

    @Override
    public List<ItemDtoResponse> getAllByUserId(Long ownerId) {
        List<Item> items = repository.findAllByOwnerIdOrderByIdAsc(ownerId);
        List<ItemDtoResponse> dtoItems = new ArrayList<>();
        for (Item item : items) {
            dtoItems.add(createItemForResponse(item.getId(), ownerId));
        }
        log.info("items by user id " + ownerId + " returned");
        return dtoItems;
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        List<ItemDto> items = new ArrayList<>();
        if (text.isBlank()) {
            return items;
        }
        items = repository.searchItem(text).stream()
                .map(ItemMapper::createDto)
                .collect(Collectors.toList());
        log.info("items returned by text: " + text);
        return items;
    }

    @Transactional
    @Override
    public ItemDto add(ItemDto item, Long id) {
        ItemRequest request = null;
        if (item.getRequestId() != null) {
            request = itemRequestRepository.findRequestById(item.getRequestId());
        }
        Item createdItem = ItemMapper.createItem(item, id, request);
        repository.save(createdItem);
        log.info("item created by user with id " + id);
        return ItemMapper.createDto(createdItem);
    }

    @Transactional
    @Override
    public Item update(ItemDto item, Long itemId, Long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("incorrect user with id " + userId);
        }
        Item updatedItem = repository.findItemById(itemId);
        if (updatedItem != null) {
            if (!updatedItem.getOwnerId().equals(userId)) {
                throw new NoRightsToUpdateException("user with id " + userId + " not own this item " + item);
            }
            if (item.getName() == null && item.getDescription() == null && item.getAvailable() == null) {
                throw new NoSuchElementException("nothing to change");
            }

            if (item.getName() != null) {
                updatedItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                updatedItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                updatedItem.setAvailable(item.getAvailable());
            }
            repository.save(updatedItem);
            log.info("item with id " + itemId + " updated");
            return updatedItem;
        } else {
            log.error("non-existent item with id " + itemId);
            throw new NoSuchElementException("can't find item by id " + itemId);
        }
    }

    @Transactional
    @Override
    public Item delete(ItemDto item, Long ownerId) {
        Item deletedItem = repository.findItemByOwnerId(ownerId);
        repository.delete(deletedItem);
        log.info("item " + item + " deleted");
        return deletedItem;
    }

    @Transactional
    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        checkBookingsForComment(itemId, userId);
        Comment comment = CommentMapper.toComment(commentDto);
        User user = userRepository.getUserById(userId);
        Item item = repository.findItemById(itemId);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        log.info("comment added to item with id " + itemId);
        return CommentMapper.toDto(comment);
    }

    private void checkBookingsForComment(Long itemId, Long userId) {
        List<Booking> bookings = bookingRepository.findBookingsByItemAndBooker(userId, itemId);
        if (bookings.size() != 0) {
            if (bookings.stream().noneMatch(t -> t.getEnd().isBefore(LocalDateTime.now()))) {
                throw new CommentAddException("can't comment before date of booking");
            }
        } else {
            throw new CommentAddException("user with id " + userId + " never books item with id " + itemId);
        }
    }

    private ItemDtoResponse createItemForResponse(Long itemId, Long ownerId) {
        Item item = repository.findItemById(itemId);
        if (item == null) {
            throw new ItemNotFoundException("can't find item by id " + itemId);
        }
        List<Booking> bookings = bookingRepository.findBookingsByItemAndOwner(itemId, ownerId);

        Booking lastBooking = bookings.stream()
                .filter(b -> b.getStart() != null).min(Comparator.comparing(Booking::getStart)).orElse(null);
        Booking nextBooking = bookings.stream()
                .reduce((first, last) -> last)
                .orElse(null);
        BookingDto last = null;
        BookingDto next = null;
        if (lastBooking != null && nextBooking != null) {
            last = BookingMapper.toDto(lastBooking);
            next = BookingMapper.toDto(nextBooking);
        }

        List<CommentDto> comments = commentRepository.findCommentsById(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());

        return ItemMapper.toItemDtoResponse(item, last, next, comments);
    }
}
