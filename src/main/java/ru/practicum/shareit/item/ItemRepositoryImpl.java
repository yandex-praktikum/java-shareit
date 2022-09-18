package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Long, Item> items;
    private Long lastId;

    @Override
    public List<Item> findItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return (new ArrayList<>(items.values())).stream()
                    .filter(item ->
                            (item.getName().toLowerCase().contains(text.toLowerCase())
                                    || item.getDescription().toLowerCase().contains(text.toLowerCase())
                            )
                                    && (item.getAvailable())).collect(Collectors.toList());
        }
    }

    public ItemRepositoryImpl() {
        this.items = new HashMap<>();
        this.lastId = Long.valueOf(0);
    }

    @Override
    public List<Item> findUserItems(Long userId) {
        return (new ArrayList<>(items.values())).stream()
                .filter(item -> isUserItem(userId, item)).collect(Collectors.toList());
    }

    private boolean isUserItem(Long userId, Item item) {
        Long itemUserId = item.getUserId();
        if (userId == null || itemUserId == null) return true;
        else return userId.equals(itemUserId);
    }


    @Override
    public Item find(Long id) {
        return items.get(id);
    }

    @Override
    public Item add(Item item) {
        item.setId(++lastId);
        items.put(lastId, item);
        return item;
    }

    @Override
    public Item update(Long id, Item item) {
        items.put(id, item);
        return item;
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

}
