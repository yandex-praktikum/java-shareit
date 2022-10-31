package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepositoryImpl itemRepository;
    private final UserServiceImpl userService;
    private final ItemMapper itemMapper;
    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        checkUserAvailability(userId);
        return itemMapper.toItemDto(itemRepository.create(itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        Item item1 = itemRepository.getById(itemId);
        if (itemDto.getAvailable() !=null){item1.setAvailable(itemDto.getAvailable());}
        if (itemDto.getName() != null){item1.setName(itemDto.getName());}
        if (itemDto.getDescription() != null){item1.setDescription(itemDto.getDescription());}
        if (itemDto.getOwner() !=null){item1.setOwner(itemDto.getOwner());}
        if (itemDto.getRequest() !=null){item1.setRequest(itemDto.getRequest());}
        itemRepository.update(item1);
        return itemMapper.toItemDto(item1);
    }
    private void checkUserAvailability (Long userId){
        try{
            userService.getById(userId);}
        catch (RuntimeException e){
            throw new NotFoundException("Юзер с ID " + userId + " не найден");}

    }
}
