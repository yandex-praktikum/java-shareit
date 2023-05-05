package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentJpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.repository.RequestJpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemServiceJpaImpl implements ItemService {

    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;

    private final BookingJpaRepository bookingRepository;

    private final CommentJpaRepository commentRepository;

    private final RequestJpaRepository requestRepository;

    private final Validator validator;

    @Override
    public List<ItemDto> getAllUserItems(int userId,  int from, int size) {
        if ((from >= 0) && (size > 0)) {
            Pageable page = PageRequest.of(0, size + from, Sort.by(Sort.Direction.ASC, "id"));
            List<ItemDto> usersItems = new ArrayList<>();
            itemRepository.findAll(userId, page).forEach(item -> usersItems.add(
                    ItemMapper.itemToItemDtoWithBookings(item,
                            BookingMapper.bookingToBookingDto(bookingRepository.findLastBookingForItem(item.getId())
                                    .orElse(new Booking())),
                            BookingMapper.bookingToBookingDto(bookingRepository.findNextBookingForItem(item.getId())
                                    .orElse(new Booking())),
                            CommentMapper.commentsToCommentDto(commentRepository.findByItemId(item.getId(),
                                    Sort.by(Sort.Direction.DESC, "id"))))));
            if (usersItems.size() > size) {
                return usersItems.subList(from, usersItems.size());
            } else {
                return usersItems;
            }
        } else {
            log.info("Параметры from и size не могут быть меньше 0");
            throw new ValidationException();
        }
    }

    @Override
    public ItemDto createItem(int userId, ItemDto itemDto) {
        if (userId > 0) {
            if ((!itemDto.getName().isBlank()) && (!itemDto.getDescription().isBlank()) &&
                    (itemDto.getAvailable() != null) && (itemDto.getDescription() != null)) {
                Item item;
                if (userRepository.findById(userId).isPresent()) {
                    if (itemDto.getRequestId() == null) {
                        item = ItemMapper.itemDtoToItem(itemDto, userRepository.findById(userId).get(),
                                null);
                    } else {
                        item = ItemMapper.itemDtoToItem(itemDto, userRepository.findById(userId).get(),
                                requestRepository.findById(itemDto.getRequestId()).get());
                    }
                } else {
                    log.info("Пользователь не существует");
                    throw new UserNotFoundException();
                }
                return ItemMapper.itemToItemDto(itemRepository.save(item));
            } else {
                log.info("Ошибка валидации вещи");
                throw new ValidationException();
            }
        } else {
            log.info("Некорректный запрос. userId должен быть больше 0");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Некорректный запрос. userId должен быть больше 0");
        }
    }

    @Override
    public ItemDto changeItem(int userId, int id, ItemDto itemDto) {
        Item updatedItem;
        if ((userId > 0) && (id > 0)) {
            if (itemRepository.findById(id).isPresent()) {
                Item item = itemRepository.findById(id).get();
                if (item.getOwner().getId() == userId) {
                    if ((itemDto.getName() == null) && (itemDto.getDescription() == null)) {
                        updatedItem = updateStatus(itemDto, item);
                    } else if (itemDto.getDescription() == null) {
                        updatedItem = updateName(itemDto, item);
                    } else if (itemDto.getName() == null) {
                        updatedItem = updateDescription(itemDto, item);
                    } else {
                        updatedItem = fullUpdateItem(itemDto, item);
                    }
                    log.info("Вещь обновлена");
                    return ItemMapper.itemToItemDto(itemRepository.save(updatedItem), new ArrayList<>());
                } else {
                    log.info("Некорректный запрос - у вещи другой владелец");
                    throw new ItemNotFoundException();
                }
            } else {
                log.info("Вещь не найдена");
                throw new ItemNotFoundException();
            }
        } else {
            log.info("Некорректный запрос. Id должен быть больше 0");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Некорректный запрос. Id должен быть больше 0");
        }
    }

    @Override
    public ItemDto findItemById(int userId, int id) {
        if ((validator.validateUser(userId, userRepository)) &&
                (validator.validateItem(id, itemRepository))) {
            Item item = itemRepository.findById(id).get();
            if (item.getOwner().getId() == userId) {
                return ItemMapper.itemToItemDtoWithBookings(item,
                        BookingMapper.bookingToBookingDto(bookingRepository.findLastBookingForItem(item.getId())
                                .orElse(new Booking())),
                        BookingMapper.bookingToBookingDto(bookingRepository.findNextBookingForItem(item.getId())
                                .orElse(new Booking())),
                        CommentMapper.commentsToCommentDto(commentRepository.findByItemId(item.getId(),
                                Sort.by(Sort.Direction.DESC, "id"))));
            } else {
                return ItemMapper.itemToItemDto(item,
                        CommentMapper.commentsToCommentDto(commentRepository.findByItemId(item.getId(),
                        Sort.by(Sort.Direction.DESC, "id"))));
            }
        } else {
            log.info("Вещь не найдена");
            throw new ItemNotFoundException();
        }
    }

    @Override
    public void removeItem(int userId, int id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getSearchedItems(String searchRequest,  int from, int size) {
        if ((from >= 0) && (size > 0)) {
            Pageable page = PageRequest.of(0, size + from, Sort.by(Sort.Direction.ASC, "id"));
            if (!searchRequest.isEmpty()) {
                List<ItemDto> searchedItems = new ArrayList<>();
                itemRepository.search(searchRequest, page).forEach(item -> searchedItems.add(ItemMapper.itemToItemDto(item,
                        new ArrayList<>())));
                if (searchedItems.size() > size) {
                    return searchedItems.subList(from, searchedItems.size());
                } else {
                    return searchedItems;
                }
            } else {
                return new ArrayList<>();
            }
        } else {
            log.info("Параметры from и size не могут быть меньше 0");
            throw new ValidationException();
        }
    }

    @Override
    public CommentDto createComment(int userId, int itemId, Comment comment) {
        if (!comment.getText().isEmpty()) {
            if ((validator.validateUser(userId, userRepository)) &&
                    (validator.validateItem(itemId, itemRepository))) {
                List<Booking> bookingList = bookingRepository.findByBookerIdAndItemId(userId, itemId,
                        Sort.by(Sort.Direction.DESC, "id"));
                if (!bookingList.isEmpty()) {
                    boolean isValid = false;
                    for (Booking booking : bookingList) {
                        if (booking.getEnd().isBefore(LocalDateTime.now())) {
                            User author = userRepository.findById(userId).get();
                            Item item = itemRepository.findById(itemId).get();
                            comment.setAuthor(author);
                            comment.setItem(item);
                            comment.setCreated(LocalDateTime.now());
                            isValid = true;
                        }
                    }
                    if (!isValid) {
                        log.info("Пользователь не может оставить комментарий, " +
                                "так как бронирование еще не завершено");
                        throw new ValidationException();
                    } else {
                        return CommentMapper.commentToCommentDto(commentRepository.save(comment));
                    }
                } else {
                    log.info("Пользователь не может оставить комментарий, " +
                            "так он не бронировал вещь");
                    throw new ValidationException();
                }
            } else {
                log.info("Некорректный запрос. Пользователь или вещь не найдены");
                throw new UserNotFoundException();
            }
        } else {
            log.info("Комментарий не может быть пустым");
            throw new ValidationException();
        }
    }

    private Item updateName(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(itemDto.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    private Item updateDescription(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(itemDto.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    private Item updateStatus(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(itemDto.getAvailable())
                .owner(item.getOwner())
                .build();
    }

    private Item fullUpdateItem(ItemDto itemDto, Item item) {
        return Item.builder()
                .id(item.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(item.getOwner())
                .build();
    }

}
