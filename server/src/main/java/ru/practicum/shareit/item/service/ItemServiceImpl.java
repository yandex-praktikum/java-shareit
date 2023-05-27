package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.ComentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectItemParameterException;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            throw new IncorrectItemParameterException("Статус не может быть пустой");
        } else if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new IncorrectItemParameterException("Название не может быть пустой");
        } else if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new IncorrectItemParameterException("Описание не может быть пустой");
        } else {
            checkOwner(ownerId);

            Item item = ItemMapper.toItem(itemDto);
            if (itemDto.getRequestId() != null) {
                item.setRequestId(itemDto.getRequestId());
            }

            item.setIsAvailable(itemDto.getAvailable());
            item.setDescription(itemDto.getDescription());
            item.setName(itemDto.getName());

            Optional<User> optionalUser = userRepository.findById(ownerId);
            item.setOwner(optionalUser.orElse(null));

            Item newItem = itemRepository.save(item);
            return ItemMapper.toItemDto(newItem);
        }
    }

    @Override
    public ItemDto update(Integer ownerId, Integer itemId, ItemDto itemDto) {
        checkOwner(ownerId);
        Optional<Item> oldItemOptional = itemRepository.findById(itemId);
        Item oldItem = oldItemOptional.orElse(null);

        if (oldItem != null && oldItem.getOwner().getId().equals(ownerId)) {
            Item item = itemRepository.findById(itemId).get();

            Optional<User> user = userRepository.findById(ownerId);
            if (user.isPresent()) {
                item.setOwner(user.get());
            }
            if (itemDto.getAvailable() != null) {
                item.setIsAvailable(itemDto.getAvailable());
            }
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getRequestId() != null) {
                item.setRequestId(itemDto.getRequestId());
            }
            Item newItem = itemRepository.save(item);
            return ItemMapper.toItemDto(newItem);
        } else {
            throw new IncorrectParameterException("Пользователь не найден");
        }
    }

    @Override
    public ItemDto getItem(Integer ownerId, Integer itemId) {
        checkOwner(ownerId);

        Optional<Item> newItem = itemRepository.findById(itemId);
        if (newItem.isPresent()) {
            Item item = newItem.get();
            ItemDto itemDto = ItemMapper.toItemDto(item);

            if (item.getOwner().getId().equals(ownerId)) {
                List<Booking> itemBookingList = bookingRepository.findByItem(item).stream()
                        .filter(booking -> booking.getStatus().equals("APPROVED"))
                        .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                        .collect(Collectors.toList());
                if (itemBookingList.size() > 1) {
                    itemDto.setLastBooking(BookingMapper.toBookingDto(itemBookingList.get(itemBookingList.size() - 1)));
                    List<Booking> itemBookingListNext = itemBookingList.stream()
                            .filter(booking -> booking.getDateBegin().after(new Date()) && booking.getStatus().equals("APPROVED"))
                            .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                            .collect(Collectors.toList());
                    if (itemBookingListNext.size() > 1) {
                        itemDto.setNextBooking(BookingMapper.toBookingDto(itemBookingListNext.get(1)));
                    }
                }
            }
            List<CommentDto> commentList = getComment(item);
            itemDto.setComments(commentList);
            return itemDto;
        } else {
            throw new IncorrectParameterException("Item не найден");
        }
    }


    @Override
    public List<ItemDto> getItems(Integer ownerId) {
        checkOwner(ownerId);
        Optional<User> owner = userRepository.findById(ownerId);
        List<Item> itemList = itemRepository.findByOwner(owner.get());

        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.stream().forEach(item -> {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            List<Booking> itemBookingList = bookingRepository.findByItem(item).stream()
                    .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                    .collect(Collectors.toList());
            if (itemBookingList.size() > 0) {
                itemDto.setLastBooking(BookingMapper.toBookingDto(itemBookingList.get(itemBookingList.size() - 1)));
                itemDto.setNextBooking(BookingMapper.toBookingDto(itemBookingList.get(itemBookingList.size() - 2)));

            }
            itemDtoList.add(itemDto);
        });

        return itemDtoList;
    }

    @Override
    public List<ItemDto> getItems(String text) {
        List<Item> list = new ArrayList<>();
        if (!text.isEmpty()) {
            list = itemRepository.findItemsLike(text.toUpperCase());
        }
        return ItemMapper.toItemDtoList(list);
    }

    private void checkOwner(Integer ownerId) {
        User user = new User();
        if (ownerId != null) {
            Optional<User> optionalUser = userRepository.findById(ownerId);
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        }
        if (user == null) {
            throw new IncorrectParameterException("Неверные параметры");
        }
    }

    @Override
    @Transactional
    public CommentDto addComment(Integer authorId, Integer itemId, CommentDto commentDto) {
        Optional<User> authorOption = userRepository.findById(authorId);
        if (!authorOption.isPresent()) {
            throw new UserNotFoundException("Автор не найден");
        }
        User author = authorOption.get();


        Optional<Item> itemOption = itemRepository.findById(itemId);
        Item item = itemOption.get();

        List<Booking> authorBooked = bookingRepository.findByItemAndBooker(item, author).stream()
                .filter(booking -> booking.getStatus().equals("APPROVED"))
                .filter(booking -> booking.getDateEnd().before(new Date()))
                .sorted(Comparator.comparing(Booking::getDateBegin).reversed())
                .collect(Collectors.toList());

        if (authorBooked.size() > 0) {
            Comment comment = new Comment();
            comment.setAuthor(author);
            comment.setDateCreated(new Date());

            comment.setItem(item);
            comment.setText(commentDto.getText());
            commentRepository.save(comment);

            CommentDto newComment = ComentMapper.toCommentDto(comment);
            newComment.setAuthorName(author.getName());
            return newComment;
        } else {
            throw new IncorrectItemParameterException("Неверные параметры");
        }
    }

    private List<CommentDto> getComment(Item item) {
        List<CommentDto> list = new ArrayList<>();
        List<Comment> itemCommentList = commentRepository.findByItem(item);
        if (itemCommentList.size() > 0) {
            list = ComentMapper.commentDtoList(itemCommentList);
        }

        return list;
    }
}
