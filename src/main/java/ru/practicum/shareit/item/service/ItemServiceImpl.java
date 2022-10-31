package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.WrongParameterException;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepositoryImpl itemRepository;
    private final UserServiceImpl userService;
    private final ItemMapper itemMapper;
    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        checkUser(userId);

        ItemDto itemDto1 = itemDto;
        itemDto1.setOwner(userId);
        return itemMapper.toItemDto(itemRepository.create(itemMapper.toItem(itemDto1)));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        checkUser(userId);
        Item item1 = itemRepository.getById(itemId);
        if (item1 != null){
            if (!(item1.getOwner().equals(userId))) {throw new NotFoundException("Изменять может только владелец");}
            if (itemDto.getAvailable() !=null){item1.setAvailable(itemDto.getAvailable());}
            if (itemDto.getName() != null){item1.setName(itemDto.getName());}
            if (itemDto.getDescription() != null){item1.setDescription(itemDto.getDescription());}
            if (itemDto.getOwner() !=null){item1.setOwner(itemDto.getOwner());}
            if (itemDto.getRequest() !=null){item1.setRequest(itemDto.getRequest());}
            itemRepository.update(item1);
            return itemMapper.toItemDto(item1);
        }
        else {
            throw  new WrongParameterException("Вещь не найдена");
        }
    }

    private void checkUser(Long userId){
        try{
            userService.getById(userId);}
        catch (RuntimeException e){
            throw new NotFoundException("Юзер с ID " + userId + " не найден");}
    }
    @Override
    public ItemDto getByID(Long id) {
        return ItemMapper.toItemDto(itemRepository.getById(id));
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
            List <ItemDto> items = new ArrayList<>();
            for (Item item : itemRepository.getAll(userId)) {
                items.add(ItemMapper.toItemDto(item));
            }
            return items;
    }
    @Override
    public List<ItemDto> searchItem(String request) {
        List <ItemDto> items = new ArrayList<>();
        for (Item item : itemRepository.searchItem(request)) {
            items.add(ItemMapper.toItemDto(item));
        }
        return items;
    }
}
