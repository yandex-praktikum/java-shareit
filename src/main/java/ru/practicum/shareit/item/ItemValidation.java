package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserValidation;

@Service
@AllArgsConstructor
public class ItemValidation {

    private final UserValidation userValidation;
    private final ItemRepository itemRepository;

    public void itemIsValidAdd(Long userId, ItemDto itemDto) {

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is null.");
        }
        userValidation.userIsPresent(userId);
        if (itemDto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is null.");
        }
        if (itemDto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is empty.");
        }
        if (itemDto.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is null.");
        }
        if (itemDto.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is empty.");
        }
        if (itemDto.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Available is null.");
        }
    }

    public void itemIsValidUpdate(Long userId, Long id) {
        userOk(userId, id);
    }

    private void userOk(Long userId, Long id) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is null.");
        }
        userValidation.userIsPresent(userId);
        Item item = itemRepository.find(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item dont find.");
        }
        if (!(userId.equals(item.getUserId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User dont own this item.");
        }
    }

    public void itemIsValidDelete(Long userId, Long id) {
        userOk(userId, id);
    }
}
