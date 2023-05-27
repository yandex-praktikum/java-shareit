package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ItemService itemService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addComment(Integer authorId, Integer itemId, CommentDto commentDto) {

        Optional<User> authorOption = userRepository.findById(authorId);
        if (!authorOption.isPresent()) {
            throw new IncorrectParameterException("Пользователь не найден");
        }

        ItemDto itemDto = itemService.getItem(authorId, itemId);
        if (itemDto.getLastBooking() != null) {
            Comment comment = new Comment();
            comment.setAuthor(authorOption.get());
            comment.setDateCreated(new Date());
            comment.setItem(ItemMapper.toItem(itemDto));
            comment.setText(commentDto.getText());
            commentRepository.save(comment);
        }
        return itemDto;
    }
}
