package ru.practicum.shareit.comment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
        List<Comment> findByItem(Item item);
}
