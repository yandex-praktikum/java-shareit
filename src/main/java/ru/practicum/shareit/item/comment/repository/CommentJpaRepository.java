package ru.practicum.shareit.item.comment.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByItemId(int itemId, Sort sort);
}
