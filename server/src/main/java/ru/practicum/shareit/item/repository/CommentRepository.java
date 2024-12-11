package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Collection<Comment> findAllByItemId(int itemId);

    Collection<Comment> findAllByItemIdIn(Collection<Integer> itemIds);
}
