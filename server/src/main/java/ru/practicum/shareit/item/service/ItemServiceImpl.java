package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.answers.model.Answer;
import ru.practicum.shareit.request.answers.repository.AnswerRepository;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.repository.RequestItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestItemRepository requestItemRepository;
    private final AnswerRepository answerRepository;

    @Override
    public List<Item> findAllItemsByUser(Long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID" + itemId + "не найдена."));
    }

    @Override
    @Transactional
    public Item createItem(Item item, Long ownerId) {
        User owner = userService.getUserById(ownerId);
        item.setOwner(owner);

        if (item.getRequest() != null && item.getRequest().getId() != null) {
            RequestItem requestItem = requestItemRepository.findById(item.getRequest().getId())
                    .orElseThrow(() -> new NotFoundException("Запрос вещи не найден"));
            item.setRequest(requestItem);
        }

        Item saved = itemRepository.save(item);

        if (item.getRequest() != null) {
            Answer answer = Answer.builder()
                    .request(item.getRequest())
                    .item(saved)
                    .build();
            answerRepository.save(answer);
        }

        return saved;
    }

    @Override
    @Transactional
    public Item updateItem(Item newItemData, Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена."));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Редактировать можно только свои вещи");
        }

        if (newItemData.getName() != null) {
            item.setName(newItemData.getName());
        }
        if (newItemData.getDescription() != null) {
            item.setDescription(newItemData.getDescription());
        }
        if (newItemData.getAvailable() != null) {
            item.setAvailable(newItemData.getAvailable());
        }

        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена."));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Удалять можно только свои вещи");
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemRepository.search(text);
    }

    @Override
    @Transactional
    public Comment addComment(Long itemId, Long userId, String text) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена."));

        User author = userService.getUserById(userId);

        bookingRepository.findFirstByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не брал эту вещь в аренду"));

        Comment comment = new Comment();
        comment.setText(text);
        comment.setItem(item);
        comment.setAuthor(author);

        return commentRepository.save(comment);
    }
}