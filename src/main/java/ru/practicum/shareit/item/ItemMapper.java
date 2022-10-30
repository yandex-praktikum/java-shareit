package ru.practicum.shareit.item;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
@Getter
public class ItemMapper {
    private Long id = 0L;

    private Long generateId(){
        ++id;
        return getId();
    }

    public Item fromItemDto (ItemDto itemDto){
        return new Item(generateId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }
}
