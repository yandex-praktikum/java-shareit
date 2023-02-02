package ru.practicum.request;

import org.springframework.stereotype.Component;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.user.User;
import ru.practicum.utilities.DateUtils;


@Component
public class ItemRequestMapper {

    public ItemRequest toRequest(ItemRequestDto requestDto, User user) {
        return ItemRequest.builder()
                .user(user)
                .created(DateUtils.now())
                .description(requestDto.getDescription())
                .build();
    }

    public ItemRequestDtoResponse toRequestDtoForResponse(ItemRequest itemRequest) {
        return ItemRequestDtoResponse.builder()
                .created(itemRequest.getCreated())
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(null)
                .build();
    }
}
