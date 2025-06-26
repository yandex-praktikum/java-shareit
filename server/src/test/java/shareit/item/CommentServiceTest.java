package shareit.item;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    private final LocalDateTime testTime = LocalDateTime.of(2025, 07, 25, 00, 00, 00);

    @Mock
    private ItemRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Spy
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @InjectMocks
    private CommentServiceImpl commentService;

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final Item item1 = Item.builder()
            .id(1L)
            .owner(user1)
            .name("item1")
            .description("desc1")
            .available(true)
            .build();

    private final Comment comment1 = Comment.builder()
            .id(1L)
            .item(item1)
            .text("text1")
            .createdAt(testTime)
            .author(user1)
            .build();

    private final Booking booking1 = Booking.builder()
            .id(1L)
            .item(item1)
            .booker(user1)
            .start(testTime.minusDays(2))
            .end(testTime.minusDays(1))
            .build();

    @Nested
    class CommentTest {
        @Test
        public void createComment() {
            when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
            when(repository.findById(item1.getId())).thenReturn(Optional.of(item1));
            when(bookingRepository.getPostBooking(eq(item1.getId()), eq(user1.getId()),
                    any(LocalDateTime.class))).thenReturn(Optional.of(booking1));
            when(commentRepository.save(any())).thenReturn(comment1);

            CommentDto dto = commentMapper.mapCommentDto(comment1);
            CommentDto result = commentService.createdComment(user1.getId(), item1.getId(), dto);

            verify(commentRepository, times(1)).save(any());

            assertEquals(comment1.getId(), result.getId());
            assertEquals(comment1.getCreatedAt(), result.getCreated());
            assertEquals(comment1.getText(), result.getText());
            assertEquals(comment1.getAuthor().getName(), result.getAuthorName());
        }

        @Test
        public void createdCommentWithFailUser() {
            Long userId = 99L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            CommentDto dto = commentMapper.mapCommentDto(comment1);

            NotFoundException ex = assertThrows(NotFoundException.class,
                    () -> commentService.createdComment(userId, item1.getId(), dto));

            assertEquals("Не удалось найти пользователя с id:99", ex.getMessage());
            verify(commentRepository, never()).save(any());
        }
    }
}
