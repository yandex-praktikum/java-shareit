package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoDate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public Item add(Item item) {
        if (userRepository.findById(item.getOwner()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        log.info("добавлена вещь /{}/", item);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Item item, Integer userId) {
        if (itemRepository.findById(item.getId()).isEmpty() || userRepository.findById(userId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!itemRepository.findById(item.getId()).orElseThrow().getOwner().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Item itemUpd = itemRepository.findById(item.getId()).orElseThrow();
        if (item.getName() != null) {
            itemUpd.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemUpd.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpd.setAvailable(item.getAvailable());
        }
        if (item.getRequestId() != null) {
            itemUpd.setRequestId(item.getRequestId());
        }
        log.info("обновлена вещь newValue=/{}/", itemUpd);
        return itemRepository.save(itemUpd);
    }

    @Override
    public Item getById(Integer id) {
        log.info("запрошена вещь id={}", id);
        return itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Collection<Item> getByNameOrDesc(String text, Integer page, Integer size) {
        if (text.equals("")) {
            return new ArrayList<Item>();
        }
        List<Item> listSearch =
                itemRepository.findByNameOrDesc(text, PageRequest.of(page, size)).toList();
        log.info("запрошен поиск по строке /{}/ ,найдено /{}/", text, listSearch.size());
        return listSearch;
    }

    @Override
    public Collection<ItemDtoDate> getAll(Integer userId, Integer page, Integer size) {
        ArrayList<ItemDtoDate> list = new ArrayList<>();
        for (Item item : itemRepository.findAllByOwnerOrderById(userId, PageRequest.of(page, size)).toList()) {
            Booking bookingLast = bookingRepository.getLastBooking(item.getId(), LocalDateTime.now());
            Booking bookNext = bookingRepository.getNextBooking(item.getId(), LocalDateTime.now());
            ItemDtoDate itemDtoDate = new ItemDtoDate();
            itemDtoDate.setId(item.getId());
            itemDtoDate.setName(item.getName());
            itemDtoDate.setDescription(item.getDescription());
            itemDtoDate.setAvailable(item.getAvailable());
            itemDtoDate.setOwner(item.getOwner());
            itemDtoDate.setLastBooking(bookingLast);
            itemDtoDate.setNextBooking(bookNext);
            list.add(itemDtoDate);
        }
        log.info("запрошены вещи владельца /{}/", userId);
        return list;
    }

    @Override
    public void delete(Integer itemId, Integer userId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDtoDate getItemDate(Integer itemId, LocalDateTime dateTime, Integer userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Booking bookingLast = bookingRepository.getLastBooking(itemId, dateTime);
        Booking bookingNext = bookingRepository.getNextBooking(itemId, dateTime);
        Set<Comment> comments = commentRepository.findAllByItem(itemId);
        Set<CommentDto> commentsDto = new HashSet<>();
        for (Comment comment : comments) {
            User user = userRepository.findById(bookingLast.getBookerId()).orElseThrow();
            commentsDto.add(commentMapper.toDto(comment, user));
        }
        ItemDtoDate itemDtoDate = new ItemDtoDate();
        itemDtoDate.setId(itemId);
        itemDtoDate.setName(item.getName());
        itemDtoDate.setDescription(item.getDescription());
        itemDtoDate.setAvailable(item.getAvailable());
        itemDtoDate.setOwner(item.getOwner());
        if (item.getOwner().equals(userId)) {
            itemDtoDate.setLastBooking(bookingLast);
            itemDtoDate.setNextBooking(bookingNext);
        }
        if (comments.size() > 0) {
            itemDtoDate.setComments(commentsDto);
        }
        log.info("запрошена вещь /{}/", itemDtoDate);
        return itemDtoDate;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Integer itemId, Integer userId) {

        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(itemId);
        comment.setAuthor(userId);
        User user = getUser(userId);
        log.info("Before findBooking>>>>>>>>>>>> comment= {} , ====user= {}", commentDto, user);
        Collection<Booking> bookings =
                bookingRepository.getByBookerAndItem(comment.getAuthor(), comment.getItem());
        for (Booking booking : bookings) {
            log.info("booking >>>>>>>>> = {}", booking);
            if (booking != null && booking.getEnd().isBefore(LocalDateTime.now())
                    && !"".equals(comment.getText())) {
                log.info("добавлен отзыв /{}/", comment);
                commentRepository.save(comment);
                return commentMapper.toDto(comment, user);
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    }

    @Override
    public User getUser(Integer id) {
        return userRepository.findById(id).orElseThrow();
    }
}
