package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new ru.practicum.shareit.item.dto.comment.CommentDto(" +
            "c.id, c.text, c.author.name, c.created) " +
            "FROM Comment AS c " +
            "WHERE c.item.id = :itemId")
    Collection<CommentDto> findAllByItemId(@Param("itemId") Long itemId);
}
