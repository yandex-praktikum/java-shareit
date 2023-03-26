package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    private User ownerUser;
    private Item item;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;

    @BeforeEach
    void beforeEach() {
        ownerUser = em.persistAndFlush(User.builder().name("name").email("e@mail.ru").build());
        item = em.persistAndFlush(Item.builder().name("дрель").description("хорошая быстрая дрель")
                .available(true).ownerId(ownerUser.getId()).build());
        comment1 = em.persistAndFlush(Comment.builder().item(item).author(ownerUser).text("комментарий 1 к дрели")
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minusMinutes(20)).build());
        comment2 = em.persistAndFlush(Comment.builder().item(item).author(ownerUser).text("комментарий 2 к дрели")
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minusMinutes(15)).build());
        comment3 = em.persistAndFlush(Comment.builder().item(item).author(ownerUser).text("комментарий 3 к дрели")
                .created(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minusMinutes(8)).build());
    }

    @Test
    void findByItem_idOrderByCreatedDescTest() {
        List<Comment> commentList = commentRepository.findByItem_idOrderByCreatedDesc(item.getId());

        assertEquals(3, commentList.size());
        assertEquals(comment3.getId(), commentList.get(0).getId());
        assertEquals(comment2.getId(), commentList.get(1).getId());
        assertEquals(comment1.getId(), commentList.get(2).getId());
    }

    @Test
    void findByItem_idInOrderByCreatedDescTest() {
        List<Comment> commentList = commentRepository.findByItem_idInOrderByCreatedDesc(List.of(item.getId()));

        assertEquals(3, commentList.size());
        assertEquals(comment3.getId(), commentList.get(0).getId());
        assertEquals(comment2.getId(), commentList.get(1).getId());
        assertEquals(comment1.getId(), commentList.get(2).getId());
    }
}