package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDto> getAllUserItems(int userId,  int from, int size);


    ItemDto createItem(int userId, ItemDto itemDto);

    ItemDto changeItem(int userId, int id, ItemDto itemDto);

    ItemDto findItemById(int userId, int id);

    void removeItem(int userId, int id);

    List<ItemDto> getSearchedItems(String searchRequest,  int from, int size);

    CommentDto createComment(int userId, int itemId, Comment comment);
}
