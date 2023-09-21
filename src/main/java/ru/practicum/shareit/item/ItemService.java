package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    ItemRepository itemRepository;
    UserRepository userRepository;

    ItemRequestRepository itemRequestRepository;
    BookingRepository bookingRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository,
                       ItemRequestRepository itemRequestRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.bookingRepository = bookingRepository;
    }

    public Item add(Long userId, Item item) {
        userRepository.containsById(userId);
        if (item.getRequestId() != null) {
            itemRequestRepository.containsById(item.getRequestId());
        }

        return itemRepository.add(userId, item);
    }

    public Item update(Long userId, ItemDto itemDto, Long itemId) {
        return itemRepository.update(userId, itemDto, itemId);
    }

    public List<Item> getAllForUser(Long userId) {
        userRepository.containsById(userId);
        return itemRepository.getAllForUser(userId);
    }

    public ItemDto getOne(Long userId, Long itemId) {
        userRepository.containsById(userId);
        return itemRepository.getOneWithoutOwner(itemId);
    }

    public List<ItemDto> search(Long userId, String text) {
        userRepository.containsById(userId);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(userId, text.toLowerCase());
    }

    public void delete(Long userId, Long itemId) {
        itemRepository.delete(userId, itemId);
    }
}
