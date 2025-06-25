package ru.practicum.shareit.item;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

@Slf4j
@Component
public class ValidateItemController {
    private final ItemRepository itemRepository;

    public ValidateItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void validateItemDto(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            log.warn("name не может быть пустым");
            throw new NotDataException("name не может быть пустым");
        }

        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            log.warn("Description не может быть пустым");
            throw new NotDataException("Description не может быть пустым");
        }
        ;
        if (itemDto.getAvailable() == null) {
            log.warn("Available не может быть пустым");
            throw new NotDataException("Available не может быть пустым");
        }

    }
}
