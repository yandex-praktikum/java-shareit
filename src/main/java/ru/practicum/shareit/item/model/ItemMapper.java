package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
@Data
@RequiredArgsConstructor(staticName = "create")
public class ItemMapper {


    public static ItemDto ItemToDto(Item item) {
        return ItemDto.create(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }

}
