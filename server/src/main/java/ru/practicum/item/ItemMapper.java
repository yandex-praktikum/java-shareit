package ru.practicum.item;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.request.ItemRequest;

import java.util.List;

public class ItemMapper {

    public static ItemDto createDto(Item item) {
        Long requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .ownerId(item.getOwnerId())
                .build();
    }

    public static Item createItem(ItemDto item, Long ownerId, ItemRequest request) {
        return Item.builder()
                .available(item.getAvailable())
                .description(item.getDescription())
                .name(item.getName())
                .ownerId(ownerId)
                .request(request)
                .build();
    }

    public static ItemDtoResponse toItemDtoResponse(Item item, BookingDto lastBooking,
                                                    BookingDto nextBooking, List<CommentDto> comments) {
        return ItemDtoResponse.builder()
                .name(item.getName())
                .id(item.getId())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .userId(item.getOwnerId())
                .build();
    }
}
