package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.AuthorDto;
import ru.practicum.shareit.user.model.User;

public class AuthorMapper {
    public static User toUser(AuthorDto authorDto) {
        return new User(authorDto.getId(), authorDto.getEmail(), authorDto.getAuthorName());
    }

    public static AuthorDto toAuthor(User user) {
        return new AuthorDto(user.getId(), user.getName(), user.getEmail());
    }
}
