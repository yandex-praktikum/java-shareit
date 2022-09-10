package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository repository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    ItemServiceImpl service;

    ItemDto dto = new ItemDto(1L, "name", "desc", true, null);
    Item item = new Item(1L, "name", "desc", true, 1L, null);

    @Test
    void shouldNotNullWhenCreate() {
        when(userService.getUserById(anyLong())).thenReturn(new User());
        when(repository.save(any())).thenReturn(item);

        ItemDto dto1 = service.create(1L, dto);

        assertEquals(dto1.getId(), item.getId());
        assertEquals(dto1.getDescription(), item.getDescription());
        assertEquals(dto1.getName(), item.getName());
        assertEquals(dto1.getAvailable(), item.getAvailable());
    }

    @Test
    void shouldNotNullWhenTryUpdateMethod() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        when(repository.save(any())).thenReturn(item);

        ItemDto dto1 = service.update(1L, 1L, dto);

        assertNotNull(dto1);
    }

    @Test
    void shouldExceptionWhenTryUpdateNotExistItem() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.update(1L, 1L, dto));
    }

    @Test
    void shouldExceptionWhenTryUpdateNotOwnerUser() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        assertThrows(NotOwnerException.class, () -> service.update(2L, 1L, dto));
    }

    @Test
    void shouldNotNullWhenTryGetItemById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(item));
        assertNotNull(service.getItemById(1L, 1L));
    }

    @Test
    void shouldExceptionWhenTryGetNotExistItem() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.getItemById(1L, 1L));
    }

    @Test
    void whenTryDeleteByIdVerify1TimesUsedDeleteMethod() {
        service.delete(1L, 1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldListSize1WhenTryGetAllUserItems() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by("id"));

        when(repository.getAllByOwnerId(1L, pageable)).thenReturn(List.of(item));

        List<ItemBookingDto> list = service.getAllUsersItems(1L, 1, 1);
        assertEquals(list.size(), 1);
    }

    @Test
    void shouldListSize1WhenTryGetSearch() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by("id"));

        when(repository.search("text", pageable)).thenReturn(List.of(item));

        List<ItemDto> list = service.search("text", 1, 1);
        assertEquals(list.size(), 1);
    }

    @Test
    void shouldNotNullWhenTryCreateComment() {
        Comment comment = new Comment();
        comment.setAuthor(new User(1L, "name", "e@e.ru"));
        comment.setText("text");
        comment.setItem(new Item());
        comment.setId(1L);

        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(Optional.of(new Booking()));
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userService.getUserById(anyLong())).thenReturn(new User(1L, "name", "e@e.ru"));
        when(commentRepository.save(any())).thenReturn(comment);

        assertNotNull(service.createComment(new CommentDto("text"), 1L, 1L));
    }

    @Test
    void shouldExceptionWhenTryCreateCommentWithNotBooking() {
        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(Optional.empty());
        assertThrows(NotAvailableException.class, () -> service.createComment(new CommentDto(), 1, 1));
    }
}


