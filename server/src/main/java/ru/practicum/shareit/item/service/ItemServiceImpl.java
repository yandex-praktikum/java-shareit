package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.PageCreatorUtil;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;
import ru.practicum.shareit.item.exception.IncorrectUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        if (itemDto.getRequestId() == null) {
            Item item = ItemMapper.toItem(itemDto, user);
            return ItemMapper.toItemDto(itemRepository.save(item));
        } else {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(() -> new RequestNotFoundException("Указанного запроса не существует"));
            Item item = ItemMapper.toItem(itemDto, user, itemRequest);
            return ItemMapper.toItemDto(itemRepository.save(item));
        }
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId) {
        Item existedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));
        if (existedItem.getOwner().getId() != ownerId) {
            throw new IncorrectUserException("Неверный владелец");
        }
        if (itemDto.getName() != null) {
            existedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existedItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(existedItem));
    }

    @Override
    public ItemDtoBookingsAndComments getItemById(int ownerId, int itemId) {
        Item existedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));

        ItemDtoBookingsAndComments itemDtoBookingsAndComments = new ItemDtoBookingsAndComments();

        List<Booking> itemBookings = bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(existedItem.getId(), BookingStatus.REJECTED);
        List<Booking> bookingsBefore = itemBookings.stream().filter(i -> i.getStart().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        List<Booking> bookingsAfter = itemBookings.stream().filter(i -> i.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(itemId);
        List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(commentList);

        if (existedItem.getOwner().getId() == ownerId) {
            ShortBookingDto lastBooking;
            if (bookingsBefore.isEmpty()) {
                lastBooking = null;
            } else {
                Booking before = bookingsBefore.get(bookingsBefore.size() - 1);
                lastBooking = new ShortBookingDto();
                lastBooking.setId(before.getId());
                lastBooking.setBookerId(before.getBooker().getId());
            }

            ShortBookingDto nextBooking;
            if (bookingsAfter.isEmpty()) {
                nextBooking = null;
            } else {
                Booking after = bookingsAfter.get(0);
                nextBooking = new ShortBookingDto();
                nextBooking.setId(after.getId());
                nextBooking.setBookerId(after.getBooker().getId());
            }

            itemDtoBookingsAndComments.setId(existedItem.getId());
            itemDtoBookingsAndComments.setName(existedItem.getName());
            itemDtoBookingsAndComments.setDescription(existedItem.getDescription());
            itemDtoBookingsAndComments.setAvailable(existedItem.isAvailable());
            itemDtoBookingsAndComments.setLastBooking(lastBooking);
            itemDtoBookingsAndComments.setNextBooking(nextBooking);
            itemDtoBookingsAndComments.setComments(commentDtoList);
        } else {
            itemDtoBookingsAndComments.setId(existedItem.getId());
            itemDtoBookingsAndComments.setName(existedItem.getName());
            itemDtoBookingsAndComments.setDescription(existedItem.getDescription());
            itemDtoBookingsAndComments.setAvailable(existedItem.isAvailable());
            itemDtoBookingsAndComments.setLastBooking(null);
            itemDtoBookingsAndComments.setNextBooking(null);
            itemDtoBookingsAndComments.setComments(commentDtoList);
        }

        return itemDtoBookingsAndComments;
    }

    @Override
    public List<ItemDtoBookingsAndComments> getUserItems(int ownerId, Integer from, Integer size) {
        PageRequest page = PageCreatorUtil.createPage(from, size);
        List<Item> userItems = itemRepository.findByOwnerId(ownerId, page);
        List<ItemDtoBookingsAndComments> resultList = new ArrayList<>();
        if (!userItems.isEmpty()) {
            for (Item item : userItems) {
                ItemDtoBookingsAndComments itemDtoBookingsAndComments = new ItemDtoBookingsAndComments();

                List<Booking> itemBookings = bookingRepository.findAllByItemIdAndStatusNotOrderByStartAsc(item.getId(), BookingStatus.REJECTED);
                List<Booking> bookingsBefore = itemBookings.stream().filter(i -> i.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
                List<Booking> bookingsAfter = itemBookings.stream().filter(i -> i.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());
                List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(item.getId());
                List<CommentDto> commentDtoList = CommentMapper.listToCommentDto(commentList);

                ShortBookingDto lastBooking;
                if (bookingsBefore.isEmpty()) {
                    lastBooking = null;
                } else {
                    Booking before = bookingsBefore.get(bookingsBefore.size() - 1);
                    lastBooking = new ShortBookingDto();
                    lastBooking.setId(before.getId());
                    lastBooking.setBookerId(before.getBooker().getId());
                }

                ShortBookingDto nextBooking;
                if (bookingsAfter.isEmpty()) {
                    nextBooking = null;
                } else {
                    Booking after = bookingsAfter.get(0);
                    nextBooking = new ShortBookingDto();
                    nextBooking.setId(after.getId());
                    nextBooking.setBookerId(after.getBooker().getId());
                }

                itemDtoBookingsAndComments.setId(item.getId());
                itemDtoBookingsAndComments.setName(item.getName());
                itemDtoBookingsAndComments.setDescription(item.getDescription());
                itemDtoBookingsAndComments.setAvailable(item.isAvailable());
                itemDtoBookingsAndComments.setLastBooking(lastBooking);
                itemDtoBookingsAndComments.setNextBooking(nextBooking);
                itemDtoBookingsAndComments.setComments(commentDtoList);

                resultList.add(itemDtoBookingsAndComments);
            }
            return resultList.stream().sorted(Comparator.comparing(ItemDtoBookingsAndComments::getId)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ItemDto> getSearch(String text, Integer from, Integer size) {
        PageRequest page = PageCreatorUtil.createPage(from, size);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> searchedItem = itemRepository.findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text, page);
        return ItemMapper.listToItemDto(searchedItem);
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, int itemId, int authorId) {
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Отсутствует текст в комментарии");
        }
        User user = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        List<Booking> bookingList = bookingRepository.findAllByItemIdAndBookerIdAndEndBefore(itemId, authorId, LocalDateTime.now());
        if (bookingList.isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду или срок аренды еще не закончился");
        }
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }
}
