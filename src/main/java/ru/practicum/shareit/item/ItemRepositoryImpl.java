package ru.practicum.shareit.item;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private Long count = 0L;
    private final Map<Long, Item> items = new HashMap<>();


    @Override
    public Item addItem(User user, Item item) {
        count++;
        item.setOwner(user);
        item.setId(count);
        items.put(count, item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item updateItem(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<ItemDto> getItemsFromUsers(Long userId) {
        return items.values().stream().filter(x -> x.getOwner().getId().equals(userId)).map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String search) {
        return items.values().stream().filter(Item::getAvailable)
                .filter(x -> x.getName().matches("(?i).*" + search + ".*") || x.getDescription().matches("(?i).*" + search + ".*"))
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
