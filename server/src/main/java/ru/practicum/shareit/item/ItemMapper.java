package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class, UserMapper.class})
public interface ItemMapper {

    default Item mapItem(ItemDto itemDto, User user , ItemRequest request) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                //.request(request)
                .owner(user)
                .request(request)
                .build();
    }

    default ItemDto mapItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner() != null ? item.getOwner().getId() : null)
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    default ItemWithCommentDto toItemWithCommentDto(Item item, List<CommentDto> comments,
                                                    BookingDto nextBooking, BookingDto lastBooking) {
        return ItemWithCommentDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .comments(comments)
                .available(item.getAvailable())
                .owner(item.getOwner() != null ? item.getOwner().getId() : null)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    default ShortItemDto toShortItemDto(Item item) {
        return ShortItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .userId(item.getOwner() != null ? item.getOwner().getId() : null)
                .build();
    }

    List<ShortItemDto> toShortItemDtoList(List<Item> items);
}
