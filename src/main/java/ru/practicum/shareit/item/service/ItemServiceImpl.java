package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.BookingForItem;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserIsNotBookerException;
import ru.practicum.shareit.item.exception.UserIsNotOwnerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.item.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository, ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        if (itemDto.getRequestId() != null) {
            item.setRequest(itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ItemRequestNotFoundException("ItemRequestNotFound")));
        }
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found")));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(long userId, Long itemId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        // нельзя изменить вещь, если ее нет в хранилище  или нет такого пользователя или вещь чужая
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        if (!owner.equals(item.getOwner())) {
            throw new UserIsNotOwnerException("Вы пытаетесь изменить чужую вещь");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Item not found"));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (userId.equals(item.getOwner().getId())) {
            setLastAndNextBookingDate(item, itemDto);
        }
        return setComments(itemDto);
    }

    @Override
    public List<ItemDto> getAllItemsByUser(int from, int size, long userId) {
        if (from < 0) {
            throw new ItemRequestNotGoodParametrsException("from < 0");
        }
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        return itemRepository.findByOwner(owner,  PageRequest.of(from / size, size))
                .stream()
                .sorted(Comparator.comparing(Item::getId))
                .map(item -> setLastAndNextBookingDate(item, ItemMapper.toItemDto(item)))
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(int from, int size, String text) {
        if (from < 0) {
            throw new ItemRequestNotGoodParametrsException("from < 0");
        }
        if (text.equals("")) {
            return new ArrayList<>();
        }
        return itemRepository.search(text,  PageRequest.of(from / size, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto setLastAndNextBookingDate(Item item, ItemDto itemDto) {
        List<BookingForItem> bookings = bookingRepository.findAllByItem(item);
        BookingForItem last = bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                .orElse(null);

        BookingForItem next = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(BookingForItem::getStart))
                .orElse(null);

        if (last != null) {
            itemDto.setLastBooking(BookingMapper.toBookingDtoItem(last));
        }
        if (next != null) {
            itemDto.setNextBooking(BookingMapper.toBookingDtoItem(next));
        }
        return itemDto;
    }

    @Override
    @Transactional
    public CommentDto createComment(long userId, long itemId, CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);
        if (comment.getText().isEmpty()) {
            throw new UserIsNotBookerException("text is empty");
        }
        comment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found")));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("item not found"));
        List<BookingForItem> bookings = bookingRepository.findAllByItem(item);
        BookingForItem booking = bookings.stream()
                .filter(bookingForItem -> bookingForItem.getBooker().getId() == userId)
                .findAny()
                .orElseThrow(() -> new UserIsNotBookerException("user not booking this item"));
        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new UserIsNotBookerException("user do not end booking;");
        }
        comment.setItem(item);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }


    public List<Comment> findCommentsByItem(long itemId) {
        return commentRepository.findCommentsByItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("item not found")));
    }

    public ItemDto setComments(ItemDto itemdto) {
        List<CommentDto> comments = findCommentsByItem(itemdto.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemdto.setComments(comments);
        return itemdto;
    }
}
