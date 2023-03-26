package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.CommentAuthorHaveNoBookingsException;
import ru.practicum.shareit.exceptions.ItemAccessDeniedException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.validators.PaginationValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.enums.BookingStatus.APPROVED;

@Component
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    /**
     * получить данные вещи по id без отзывов и без данных о бронированиях
     *
     * @param itemId id вещи
     * @return объект типа Item
     * @throws ItemNotFoundException если вещь с таким id не найдена
     */
    public Item getById(long itemId) throws ItemNotFoundException {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ItemNotFoundException("Вещь " + itemId + " не найдена");
        } else {
            return optionalItem.get();
        }
    }

    /**
     * Получить объект Item с отзывами, а для владельца вещи будут добавлены данные о предыдущем и следующем бронировании
     *
     * @param itemId  id вещи
     * @param ownerId id текущего пользователя
     * @return объект типа Item
     */
    public Item getById(long itemId, long ownerId) {
        Item item = getById(itemId);
        if (item.getOwnerId() == ownerId) {
            updateItemsWithBookings(List.of(item));
        }
        item.setComments(commentRepository.findByItem_idOrderByCreatedDesc(item.getId()));
        return item;
    }

    /**
     * получить список вещей, принадлежащих пользователю с указанным id
     *
     * @param ownerId id пользователя
     * @return список объектов типа Item
     */
    public List<Item> getOwnedItemsList(long ownerId, int from, int size) {
        Pageable page = PaginationValidator.validate(from, size);
        List<Item> itemList = itemRepository.findByOwnerIdOrderById(ownerId, page);
        updateItemsWithBookings(itemList);
        updateItemsWithComments(itemList);

        return itemList;
    }

    /**
     * метод загружает в объекты Item данные об отзывах
     *
     * @param itemList список объектов типа Item
     */
    private void updateItemsWithComments(List<Item> itemList) {
        if (!itemList.isEmpty()) {
            List<Long> itemIdList = itemList.stream().map(Item::getId).collect(Collectors.toList());
            Map<Long, Item> itemMap = itemList.stream().collect(Collectors.toMap(Item::getId, item -> item));

            List<Comment> commentsList = commentRepository.findByItem_idInOrderByCreatedDesc(itemIdList);
            for (Comment comment : commentsList) {
                Item item = itemMap.get(comment.getItem().getId());

                item.getComments().add(comment);
            }
        }
    }

    /**
     * метод загружает в объекты Item данные о предыдущем и следующем бронировании
     *
     * @param itemList список объектов типа Item
     */
    private void updateItemsWithBookings(List<Item> itemList) {
        if (!itemList.isEmpty()) {
            LocalDateTime nowDateTime = LocalDateTime.now();
            List<Long> itemIdList = itemList.stream().map(Item::getId).collect(Collectors.toList());
            Map<Long, Item> itemMap = itemList.stream().collect(Collectors.toMap(Item::getId, item -> item));

            List<Booking> bookingsList = bookingRepository.findByItem_idIn(itemIdList);
            for (Booking booking : bookingsList) {
                Item item = itemMap.get(booking.getItem().getId());

                Booking lastBooking = item.getLastBooking();
                if (booking.getRentEndDate().isBefore(nowDateTime)
                        && (lastBooking == null
                        || lastBooking.getRentEndDate().isBefore(booking.getRentEndDate()))) {
                    item.setLastBooking(booking);
                }

                Booking nextBooking = item.getNextBooking();
                if (booking.getRentStartDate().isAfter(nowDateTime)
                        && (nextBooking == null
                        || nextBooking.getRentStartDate().isAfter(booking.getRentStartDate()))) {
                    item.setNextBooking(booking);
                }
            }
        }
    }

    /**
     * сохранить новую вещь в хранилище, присвоить уникальный id
     *
     * @param itemCreateRequest заполненный объект ItemCreateRequest
     * @param ownerId           id пользователя, который будет указан владельцем вещи
     * @return заполненный объект Item
     */
    public Item create(ItemCreateRequest itemCreateRequest, long ownerId) {
        userService.getById(ownerId);

        Item item = ItemDtoMapper.toItem(itemCreateRequest);
        item.setOwnerId(ownerId);

        long requestId = itemCreateRequest.getRequestId();
        if (requestId > 0) {
            item.setItemRequest(itemRequestRepository.getReferenceById(requestId));
        }

        return itemRepository.save(item);
    }

    /**
     * изменить данные вещи с указанным id
     *
     * @param item    заполненный объект Item
     * @param ownerId id пользователя - владельца вещи
     * @return заполненный объект Item
     */
    public Item update(Item item, long ownerId) {
        if (item != null && item.getId() > 0) {
            Item storageItem = getById(item.getId());
            if (storageItem.getOwnerId() == ownerId) {
                if (item.getName() != null && !item.getName().isBlank()) {
                    storageItem.setName(item.getName());
                }
                if (item.getDescription() != null && !item.getDescription().isBlank()) {
                    storageItem.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    storageItem.setAvailable(item.getAvailable());
                }

                return itemRepository.save(storageItem);
            } else {
                throw new ItemAccessDeniedException("Вещь принадлежит другому пользователю");
            }
        }
        return null;
    }

    /**
     * удалить вещь с указанным id из хранилища
     *
     * @param itemId  id вещи
     * @param ownerId id пользователя - владельца вещи
     */
    void delete(long itemId, long ownerId) {
        Item savedItem = getById(itemId);
        if (savedItem.getOwnerId() != ownerId) {
            throw new ItemAccessDeniedException("Вещь принадлежит другому пользователю");
        }
        itemRepository.delete(savedItem);
    }

    /**
     * поиск строки в названии или описании вещей
     *
     * @param text строка поиска
     * @return список объектов Item, которые содержат искомую строку в названиях или описаниях
     */
    public List<Item> search(String text, int from, int size) {
        if (!text.isBlank()) {
            Pageable page = PaginationValidator.validate(from, size);
            List<Item> searchResults = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true, page);
            return searchResults;
        } else {
            return List.of();
        }
    }

    /**
     * метод сохраняет новый отзыв к вещи
     *
     * @param text     текст отзыва
     * @param itemId   id вещи
     * @param authorId id пользователя
     * @return объект типа Comment
     * @throws CommentAuthorHaveNoBookingsException если у пользователя нет завершенных бронирований этой вещи (не может оставлять отзыв)
     */
    public Comment createComment(String text, long itemId, long authorId) throws CommentAuthorHaveNoBookingsException {
        Item item = getById(itemId);
        User author = userService.getById(authorId);

        if (authorHaveBookingsOfItem(itemId, authorId)) {
            Comment comment = new Comment(0, text, item, author, LocalDateTime.now());
            return commentRepository.save(comment);
        } else {
            throw new CommentAuthorHaveNoBookingsException("Пользователь " + authorId + " не имеет завершенных бронирований вещи " + itemId);
        }
    }

    /**
     * метод для поиска одобренных завершенных бронирований вещи
     *
     * @param itemId   id вещи
     * @param authorId id пользователя
     * @return true если бронирования были, false если бронирований не было
     */
    private boolean authorHaveBookingsOfItem(long itemId, long authorId) {
        List<Booking> bookings = bookingRepository.findByItem_idAndBooker_idAndStatusAndRentEndDateIsBefore(itemId, authorId, APPROVED, LocalDateTime.now());
        return !bookings.isEmpty();
    }
}