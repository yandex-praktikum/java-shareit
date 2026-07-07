package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.mapper.ItemMapperMapstruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemRequestDto;
import ru.practicum.shareit.item.model.ItemResponseDto;
import ru.practicum.shareit.item.comments.model.CommentDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapperMapstruct itemMapper;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;

    @GetMapping
    public List<ItemResponseDto> findAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findAllItemsByUser(ownerId).stream()
                .map(this::enrichWithBookingsAndComments)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto findItemById(@PathVariable Long itemId) {
        Item item = itemService.findItemById(itemId);
        return enrichWithBookingsAndComments(item);
    }

    @PostMapping
    public ItemResponseDto createNewItem(@RequestBody ItemRequestDto dto,
                                         @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item item = itemMapper.mapToItem(dto, null);
        Item created = itemService.createItem(item, ownerId);
        return itemMapper.mapToDto(created);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestBody ItemRequestDto dto,
                                      @PathVariable Long itemId,
                                      @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item newItemData = itemMapper.mapToItem(dto, null);
        Item updated = itemService.updateItem(newItemData, itemId, ownerId);
        return itemMapper.mapToDto(updated);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemService.deleteItem(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text).stream()
                .map(itemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody String text) {
        Comment comment = itemService.addComment(itemId, userId, text);
        return commentMapper.toDto(comment);
    }

    private ItemResponseDto enrichWithBookingsAndComments(Item item) {
        LocalDateTime now = LocalDateTime.now();
        ItemResponseDto dto = itemMapper.mapToDto(item);

        bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now)
                .ifPresent(booking -> dto.setLastBooking(bookingMapper.toDto(booking)));

        bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(item.getId(), now)
                .ifPresent(booking -> dto.setNextBooking(bookingMapper.toDto(booking)));

        dto.setComments(
                commentRepository.findAllByItemId(item.getId()).stream()
                        .map(commentMapper::toDto)
                        .toList()
        );

        return dto;
    }
}