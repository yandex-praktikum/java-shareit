package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found"));
        Item item = itemMapper.fromDto(itemDto);
        item.setOwner(owner);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);

    }

    public ItemDto getItem(long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with id " + id + " not found"));
        return itemMapper.toDto(item);
    }

    public List<ItemDto> getAllItemsFromUser(long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String search) {
        if (search == null || search.isEmpty()) {
            return new ArrayList<ItemDto>();
        }
        return itemRepository.findByNameContainingIgnoreCase(search).stream()
                .map(itemMapper::toDto)
                .filter(ItemDto::getAvailable)
                .collect(Collectors.toList());
    }

    public ItemDto updateItem(long id, long userId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found"));
        return itemRepository.findById(id).map(existingItem -> {
            boolean isUpdated = false;
            if (existingItem.getOwner().getId() != userId) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Item with id " + id + " not related to user with id " + userId);
            }
            if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
                existingItem.setName(itemDto.getName());
                isUpdated = true;
            }
            if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
                existingItem.setDescription(itemDto.getDescription());
                isUpdated = true;
            }
            if (itemDto.getAvailable() != null) {
                existingItem.setAvailable(itemDto.getAvailable());
                isUpdated = true;
            }
            if (isUpdated) {
                itemRepository.save(existingItem);
                return itemMapper.toDto(existingItem);
            } else {
                return itemMapper.toDto(existingItem);
            }

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with id " + id + " not found"));

    }
}
