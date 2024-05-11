package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    void findAllByItemIdOrderByCreatedAscTest() throws Exception {
        User user1 = new User(1, "User", "user@mail.ru");
        ItemRequest itemRequest1 = new ItemRequest("description1", user1, LocalDateTime.now());
        Item item1 = new Item(1, "item1", "description1", true, user1, itemRequest1);
        Comment comment1 = new Comment(1, "comment1", item1, user1, LocalDateTime.of(2000, 2, 1, 10, 0));
        Comment comment2 = new Comment(2, "comment2", item1, user1, LocalDateTime.of(2000, 1, 30, 10, 0));
        Comment comment3 = new Comment(3, "comment3", item1, user1, LocalDateTime.of(2000, 1, 1, 10, 0));

        userRepository.save(user1);
        itemRequestRepository.save(itemRequest1);
        itemRepository.save(item1);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        List<Comment> commentList = commentRepository.findAllByItemIdOrderByCreatedAsc(1);

        Assertions.assertEquals(3, commentList.size());
        Assertions.assertEquals("comment3", commentList.get(0).getText());
        Assertions.assertEquals("comment2", commentList.get(1).getText());
        Assertions.assertEquals("comment1", commentList.get(2).getText());
    }
}
