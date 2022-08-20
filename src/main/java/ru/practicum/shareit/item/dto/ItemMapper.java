package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mopel.User;

@Component
@AllArgsConstructor
public class ItemMapper {

    public ItemInputDto toInputDto(Item item) {
        Long id = item.getId();
        String name = item.getName();
        String description = item.getDescription();
        Boolean available = item.getAvailable();

        return new ItemInputDto(id, name, description, available);
    }

    public ItemOutputDto toOutputDto(Item item) {
        ItemOutputDto itemOutputDto = new ItemOutputDto();

        itemOutputDto.setId(item.getId());
        itemOutputDto.setName(item.getName());
        itemOutputDto.setDescription(item.getDescription());
        itemOutputDto.setAvailable(item.getAvailable());

        return itemOutputDto;
    }

    public Item toItem(ItemInputDto itemInputDto, User owner) {
        Long id = itemInputDto.getId();
        String name = itemInputDto.getName();
        String description = itemInputDto.getDescription();
        Boolean available = itemInputDto.getAvailable();

        return new Item(id, name, description, available, owner);
    }

}
