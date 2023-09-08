package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public Request convertToRequest(RequestDto requestDto) {
        return Request.builder()
                .description(requestDto.getDescription())
                .build();
    }

    public RequestDto convertToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(request.getItems() == null ? null :
                        request.getItems()
                                .stream()
                                .map(ItemMapper::convertToItemShortDto)
                                .collect(Collectors.toList()))
                .build();
    }
}
