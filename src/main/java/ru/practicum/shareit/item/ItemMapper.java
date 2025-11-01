package ru.practicum.shareit.item;


/**
 * Преобразование
 */

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setOwnerId(item.getOwner() != null ? item.getOwner().getId() : null);
        itemDto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);

        return itemDto;
    }

    public static Item toItem (ItemDto itemDto){
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

}
