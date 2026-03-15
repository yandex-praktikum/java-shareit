package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final Map<Long, Item> repository = new HashMap<>();
    private final UserService userService;
    private Long lastGeneratedId = 1L;

    @Override
    public List<ItemDto> search(String searchStr) {
        if (searchStr == null || searchStr.isBlank()) {
            return Collections.emptyList();
        }

        String lowerCaseQuery = searchStr.toLowerCase();
        return repository.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerCaseQuery)
                        || item.getDescription().toLowerCase().contains(lowerCaseQuery))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(Long id) {
        Item found = repository.get(id);
        if (found == null) {
            throw new NotFoundException("Объект с идентификатором " + id + " не существует");
        }
        return ItemMapper.toItemDto(found);
    }

    @Override
    public List<ItemDto> getByOwner(Long ownerId) {
        userService.getById(ownerId);

        return repository.values().stream()
                .filter(obj -> obj.getOwner().getId().equals(ownerId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto dto) {
        Item target = repository.get(itemId);

        if (Objects.isNull(target)) {
            throw new NotFoundException("Вещь с id " + itemId + " не найдена");
        }

        if (!Objects.equals(target.getOwner().getId(), userId)) {
            throw new NotFoundException("Доступ запрещен: редактировать может только владелец");
        }

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            target.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {
            target.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            target.setAvailable(dto.getAvailable());
        }

        return ItemMapper.toItemDto(target);
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        User creator = UserMapper.toUser(userService.getById(userId));
        Item newEntry = ItemMapper.toItem(itemDto);

        newEntry.setId(lastGeneratedId++);
        newEntry.setOwner(creator);

        repository.put(newEntry.getId(), newEntry);
        log.info("Сохранена новая вещь с ID: {}", newEntry.getId());

        return ItemMapper.toItemDto(newEntry);
    }
}