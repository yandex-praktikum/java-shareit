package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> get(@RequestHeader("X-Sharer-User-Id") int userId) {
        if (userId == 0){
            return itemService.getAllItems();
        } else {
            return itemService.getItemsByUserId(userId);
        }
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") int userId,
                    @Valid @RequestBody Item item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int itemId,
                           @Valid @RequestBody Item item){
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId){
        return itemService.getItemById(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<Item> findItemByRequest(@RequestParam (name = "text") String text){
        return itemService.findItemByRequest(text);
    }
}