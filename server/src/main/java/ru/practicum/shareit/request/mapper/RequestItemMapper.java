package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.model.RequestItemRequestDto;
import ru.practicum.shareit.request.model.RequestItemResponseDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface RequestItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestor", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "answers", ignore = true)
    RequestItem mapToRequestItem(RequestItemRequestDto dto);

    @Mapping(target = "requestorId", source = "requestor.id")
    @Mapping(target = "created", source = "created", qualifiedByName = "instantToString")
    RequestItemResponseDto mapToDto(RequestItem requestItem);

    @Named("instantToString")
    default String formatData(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy.MM.dd, hh:mm:ss")
                .withZone(ZoneId.of("UTC"));

        return formatter.format(instant);
    }
}