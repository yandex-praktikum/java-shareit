package ru.practicum.shareit.item.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.model.CommentDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "created", source = "created", qualifiedByName = "instantToString")
    CommentDto toDto(Comment comment);

    @Named("instantToString")
    default String formatInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy.MM.dd, hh:mm:ss")
                .withZone(ZoneId.of("UTC"));
        return formatter.format(instant);
    }
}