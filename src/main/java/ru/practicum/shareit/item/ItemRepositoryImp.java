package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.MainData;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImp implements ItemRepository {
    private final List<Item> items;
    private final List<Booking> bookings;
    private final List<Review> reviews;
    private Long globalId = 1L;

    public ItemRepositoryImp(MainData mainData) {
        this.items = mainData.getItems();
        this.bookings = mainData.getBookings();
        this.reviews = mainData.getReviews();
    }

    @Override
    public Item add(Long userId, Item item) {
        item.setOwnerId(userId);
        item.setId(globalId);
        globalId++;
        items.add(item);
        return item;
    }

    @Override
    public Item getOne(Long itemId) {
        return items.stream().filter(item -> item.getId().equals(itemId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("item по id - " + itemId + " не найден"));
    }

    @Override
    public Item update(Long userId, ItemDto itemDto, Long itemId) {
        containsSameOwner(userId, itemId);

        Item oldItemToUpdate = getOne(itemId);
        if (itemDto.getName() != null) {
            oldItemToUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItemToUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItemToUpdate.setAvailable(itemDto.getAvailable());
        }
        return oldItemToUpdate;
    }

    @Override
    public List<Item> getAllForUser(Long userId) {
        return items.stream().filter(item -> item.getOwnerId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public ItemDto getOneWithoutOwner(Long itemId) {
        return toItemDto(getOne(itemId));
    }

    @Override
    public void containsSameOwner(Long userId, Long itemId) {
        Long ownerId = getOne(itemId).getOwnerId();
        if (!ownerId.equals(userId)) {
            throw new EntityNotFoundException("user - " + userId + " не владеет item - " + itemId);
        }
    }

    @Override
    public void containsById(Long itemId) {
        items.stream().filter(item -> item.getId().equals(itemId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("item по id - " + itemId + " не найден"));
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        return items.stream().filter(Item::getAvailable)
                .filter(item -> item.getDescription().toLowerCase().contains(text) || item.getName().toLowerCase().contains(text))
                .map(this::toItemDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long itemId) {
        containsSameOwner(userId, itemId);
        items.removeIf(item -> item.getId().equals(itemId));
        bookings.removeIf(booking -> booking.getItemId().equals(itemId));
        reviews.removeIf(review -> review.getReviewedItemId().equals(itemId));
    }

    private ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }
}
