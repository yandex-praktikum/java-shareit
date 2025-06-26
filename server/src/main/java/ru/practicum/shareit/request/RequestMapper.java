package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.dto.FullRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, })
public interface RequestMapper {

    default RequestDto mapRequestDto(ItemRequest request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .description(request.getDescription())
                .userId(request.getRequester().getId())
                .build();
    }

    List<RequestDto> mapRequestDto(List<ItemRequest> items);

    default ItemRequest mapItemRequest(RequestDto requestDto, User user) {
        return ItemRequest.builder()
                .id(requestDto.getId())
                .requester(user)
                .description(requestDto.getDescription())
                .created(requestDto.getCreated())
                .build();
    }

    default FullRequestDto mapFullRequestDto(ItemRequest request, List<ShortItemDto> items) {
        return FullRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .description(request.getDescription())
                .userId(request.getRequester().getId())
                .items(items)
                .build();
    }
}
