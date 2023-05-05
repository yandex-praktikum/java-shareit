package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ItemDto> getAllUserItems(int userId,  int from, int size) {
        List<ItemDto> userItems = new ArrayList<>();
        itemRepository.getAllItems(userId).forEach(item -> userItems.add(ItemMapper.itemToItemDto(item,
                new ArrayList<>())));
        return userItems;
    }

    @Override
    public ItemDto createItem(int userId, ItemDto itemDto) {
        if (userId > 0) {
            Item item;
            if (userRepository.findById(userId).isPresent()) {
                item = ItemMapper.itemDtoToItem(itemDto, userRepository.findById(userId).get(), null);
            } else {
                throw new UserNotFoundException();
            }
            return itemRepository.add(userId, item);
        } else {
            log.info("Некорректный запрос. userId должен быть больше 0");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Некорректный запрос. userId должен быть больше 0");
        }
    }

    @Override
    public ItemDto changeItem(int userId, int id, ItemDto itemDto) {
        if ((userId > 0) && (id > 0)) {
            return itemRepository.update(userId, id, itemDto);
        } else {
            log.info("Некорректный запрос. Id должен быть больше 0");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Некорректный запрос. Id должен быть больше 0");
        }
    }

    @Override
    public ItemDto findItemById(int userId, int id) {
        return itemRepository.findById(userId, id).orElseThrow(() -> new ItemNotFoundException());
    }

    @Override
    public void removeItem(int userId, int id) {
        itemRepository.delete(userId, id);
    }

    @Override
    public List<ItemDto> getSearchedItems(String searchRequest,  int from, int size) {
        if (!searchRequest.isEmpty()) {
            return itemRepository.findItems(searchRequest);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDto createComment(int userId, int itemId, Comment comment) {
        return new CommentDto();
    }

}
