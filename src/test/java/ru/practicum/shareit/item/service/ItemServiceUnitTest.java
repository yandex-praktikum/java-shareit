package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.AuthorDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.ComentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectItemParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {
    private Date dateBeg = getDate("2023-03-29");
    private Date dateEnd = getDate("2023-04-15");

    private final BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final CommentRepository mockCommentRepository = Mockito.mock(CommentRepository.class);

    private final User owner = new User(1, "eee@email.ru", "Eva");
    private final Item item = new Item(1, "carpet", "description", true, null, owner);

    private final User booker = new User(2, "ppp@email.ru", "Polina");
    private final Booking booking = new Booking(1, dateBeg, dateEnd, item, booker, "APPROVED");

    private final ItemService itemService = new ItemServiceImpl(mockUserRepository, mockBookingRepository, mockItemRepository, mockCommentRepository);

    @Test
    public void shouldNotViewItemBookingsForOtherUser() {
        Mockito.when(mockBookingRepository.findByItem(item)).thenReturn(List.of(booking));
        Mockito.when(mockItemRepository.findById(1)).thenReturn(Optional.of(item));
        User otherUser = new User(3, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(2)).thenReturn(Optional.of(otherUser));

        ItemDto itemDto = itemService.getItem(2, 1);
        Assertions.assertNull(itemDto.getLastBooking());
    }

    @Test
    public void shouldFailAddItemWithIncorrectParam() {
        ItemDto newItem = new ItemDto(null, null, null, null, null, null, null, null);
        IncorrectItemParameterException exception = Assertions.assertThrows(IncorrectItemParameterException.class, () -> itemService.addItem(owner.getId(), newItem));
        Assertions.assertNotNull(exception);

        ItemDto newItemWithoutName = new ItemDto(null, null, null, true, null, null, null, null);
        exception = Assertions.assertThrows(IncorrectItemParameterException.class, () -> itemService.addItem(owner.getId(), newItemWithoutName));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(exception.getParameter(), "Название не может быть пустой");

        ItemDto newItemWithoutDescription = new ItemDto(null, "name", null, true, null, null, null, null);
        exception = Assertions.assertThrows(IncorrectItemParameterException.class, () -> itemService.addItem(owner.getId(), newItemWithoutDescription));
        Assertions.assertNotNull(exception, "Описание не может быть пустой");
    }

    @Test
    public void shouldViewItemBookingsForOwnerWIthMultipleBookings() {
        Mockito.when(mockItemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        Mockito.when(mockUserRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        dateBeg = getDate("2022-10-29");
        dateEnd = getDate("2022-11-15");
        Booking booking1 = new Booking(2, dateBeg, dateEnd, item, booker, "APPROVED");

        dateBeg = getDate("2022-12-29");
        dateEnd = getDate("2023-01-15");
        Booking booking2 = new Booking(3, dateBeg, dateEnd, item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findByItem(item)).thenReturn(List.of(booking, booking1, booking2));

        ItemDto itemDto = itemService.getItem(1, 1);
        Assertions.assertNotNull(itemDto.getLastBooking());
        Assertions.assertEquals(itemDto.getLastBooking().getId(), booking2.getId());
    }

    @Test
    public void shouldAddComment() {
        User author = new User(3, "sabrina@email.ru", "Sabrina");
        Mockito.when(mockUserRepository.findById(author.getId())).thenReturn(Optional.of(author));
        Mockito.when(mockItemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        dateBeg = getDate("2023-02-21");
        dateEnd = getDate("2023-03-01");
        Booking authorBooking = new Booking(2, dateBeg, dateEnd, item, author, "APPROVED");
        Mockito.when(mockBookingRepository.findByItemAndBooker(item, author)).thenReturn(List.of(authorBooking));

        AuthorDto authorDto = new AuthorDto(author.getId(), author.getName(), author.getEmail());
        CommentDto comment = new CommentDto(null, "this is test comment", item, authorDto, authorDto.getAuthorName(), new Date());
        CommentDto newComment = itemService.addComment(author.getId(), item.getId(), comment);
        Assertions.assertNotNull(newComment);
    }

    @Test
    public void shouldMapToCommentDtoList() {
        User author = new User(3, "sabrina@email.ru", "Sabrina");
        Comment comment1 = new Comment(1, "text1", item, author, new Date());
        Comment comment2 = new Comment(1, "text2", item, author, new Date());
        List<Comment> commentList = List.of(comment1, comment2);
        List<CommentDto> commentDto = ComentMapper.commentDtoList(commentList);
        Assertions.assertNotNull(commentDto);
        Assertions.assertEquals(commentDto.get(0).getText(), comment1.getText());
        Assertions.assertEquals(commentDto.get(1).getText(), comment2.getText());
    }

    @Test
    public void shouldMapToComment() {
        User author = new User(3, "sabrina@email.ru", "Sabrina");
        AuthorDto authorDto = new AuthorDto(author.getId(), author.getName(), author.getEmail());
        CommentDto commentDto = new CommentDto(null, "this is test comment", item, authorDto, authorDto.getAuthorName(), new Date());
        Comment comment = ComentMapper.toComment(commentDto);
        Assertions.assertNotNull(comment);
        Assertions.assertEquals(comment.getText(), commentDto.getText());
        Assertions.assertEquals(comment.getDateCreated(), commentDto.getCreated());
        Assertions.assertEquals(comment.getItem().getName(), commentDto.getItem().getName());
    }

    @Test
    public void shouldSuccessGetItems() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(owner));
        Mockito.when(mockItemRepository.findByOwner(any())).thenReturn(List.of(item));
        dateBeg = getDate("2022-10-29");
        dateEnd = getDate("2022-11-15");
        Booking booking1 = new Booking(2, dateBeg, dateEnd, item, booker, "APPROVED");

        dateBeg = getDate("2022-12-29");
        dateEnd = getDate("2023-01-15");
        Booking booking2 = new Booking(3, dateBeg, dateEnd, item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findByItem(item)).thenReturn(List.of(booking, booking1, booking2));

        List<ItemDto> list = itemService.getItems(owner.getId(), null, null);

        Assertions.assertNotNull(list);
        Assertions.assertNotNull(list.get(0).getLastBooking());
        Assertions.assertNotNull(list.get(0).getNextBooking());
    }

    @Test
    public void shouldSuccessGetItemsByPage() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(owner));
        Mockito.when(mockItemRepository.findByOwner(any(), any())).thenReturn(new PageImpl<>(List.of(item)));
        dateBeg = getDate("2022-10-29");
        dateEnd = getDate("2022-11-15");
        Booking booking1 = new Booking(2, dateBeg, dateEnd, item, booker, "APPROVED");

        dateBeg = getDate("2022-12-29");
        dateEnd = getDate("2023-01-15");
        Booking booking2 = new Booking(3, dateBeg, dateEnd, item, booker, "APPROVED");
        Mockito.when(mockBookingRepository.findByItem(item)).thenReturn(List.of(booking, booking1, booking2));

        List<ItemDto> list = itemService.getItems(owner.getId(), 1, 3);

        Assertions.assertNotNull(list);
        Assertions.assertNotNull(list.get(0).getLastBooking());
        Assertions.assertNotNull(list.get(0).getNextBooking());
    }

    @Test
    public void shouldSuccessUpdateItem() {
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(owner));

        Item oldItem = new Item(2, "pled", "warm", true, null, owner);
        Mockito.when(mockItemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        Mockito.when(mockItemRepository.save(any())).thenReturn(item);

        ItemDto newItem = new ItemDto(2, "плед", "теплый", true, null, null, null, null);
        Assertions.assertNotEquals(oldItem.getName(), newItem.getName());
        Assertions.assertNotEquals(oldItem.getDescription(), newItem.getDescription());

        ItemDto updatedItem = itemService.update(owner.getId(), oldItem.getId(), newItem);

        Assertions.assertEquals(updatedItem.getName(), newItem.getName());
        Assertions.assertEquals(updatedItem.getDescription(), newItem.getDescription());
    }

    private Date getDate(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
