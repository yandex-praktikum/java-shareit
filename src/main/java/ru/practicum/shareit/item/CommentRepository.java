package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Set<Comment> findAllByItem(Integer itemId);
}
