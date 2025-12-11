package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        checkUserExists(ownerId);

        Item item = ItemMapper.toItem(itemDto, ownerId);
        Item savedItem = itemRepository.save(item);

        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new EntityNotFoundException("Вещь с ID " + itemId + " не принадлежит пользователю " + ownerId);
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

        Item updatedItem = itemRepository.save(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь с ID " + itemId + " не найдена"));
        return enhanceItemWithAdditionalData(item, userId);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        checkUserExists(ownerId);
        return itemRepository.findByOwnerId(ownerId).stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .map(item -> enhanceItemWithAdditionalData(item, ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text, Long userId) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.searchAvailableByNameOrDescription(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private ItemDto enhanceItemWithAdditionalData(Item item, Long userId) {
        ItemDto itemDto = ItemMapper.toItemDto(item);


        if (item.getOwnerId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();


            List<Booking> lastBookings = bookingRepository.findLastBookingForItem(item.getId(), now);
            if (!lastBookings.isEmpty()) {
                Booking lastBooking = lastBookings.get(0);
                itemDto.setLastBooking(new BookingDto(
                        lastBooking.getId(),
                        lastBooking.getBookerId(),
                        lastBooking.getStart(),
                        lastBooking.getEnd()
                ));
            }


            List<Booking> nextBookings = bookingRepository.findNextBookingForItem(item.getId(), now);
            if (!nextBookings.isEmpty()) {
                Booking nextBooking = nextBookings.get(0);
                itemDto.setNextBooking(new BookingDto(
                        nextBooking.getId(),
                        nextBooking.getBookerId(),
                        nextBooking.getStart(),
                        nextBooking.getEnd()
                ));
            }
        }


        List<Comment> comments = commentRepository.findByItemId(item.getId());
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> {
                    User author = userRepository.findById(comment.getAuthorId())
                            .orElseThrow(() -> new EntityNotFoundException("Автор комментария не найден"));
                    return new CommentDto(
                            comment.getId(),
                            comment.getText(),
                            author.getName(),
                            comment.getCreated()
                    );
                })
                .collect(Collectors.toList());

        itemDto.setComments(commentDtos);

        return itemDto;
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }
}
