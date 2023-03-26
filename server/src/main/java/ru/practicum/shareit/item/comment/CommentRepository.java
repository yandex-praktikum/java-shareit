package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItem_idOrderByCreatedDesc(long id);

    List<Comment> findByItem_idInOrderByCreatedDesc(List<Long> itemIdList);
}