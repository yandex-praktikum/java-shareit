package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.WrongParameterException;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepositoryImpl itemRepository;
    private final UserServiceImpl userService;
    private final ItemMapper itemMapper;
    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
            try{
                userService.getById(userId);}
            catch (RuntimeException e){
                throw new NotFoundException("Юзер с ID " + userId + " не найден");}

        return itemMapper.toItemDto(itemRepository.create(itemMapper.toItem(itemDto)));
    }
}
