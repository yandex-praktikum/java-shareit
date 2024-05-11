package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.PaginationException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    ItemRequestRepository itemRequestRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Test
    void addItemRequestTest() {
        User user = new User(1, "user", "user@mail.ru");
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "description", LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        itemRequestService.addItemRequest(itemRequestDto, 1);

        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void getAllRequesterItemRequest_NotEmptyRequestsAndItems() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        Item item = new Item(1, "item", "description", true, owner, itemRequest);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(List.of(itemDtoForRequest));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequesterId(1)).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(1)).thenReturn(List.of(item));

        List<ItemRequestDto> result = itemRequestService.getAllRequesterItemRequests(1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(1, result.get(0).getItems().get(0).getId());
    }

    @Test
    void getAllRequesterItemRequest_NotEmptyRequestsAndEmptyItems() {
        User user = new User(1, "user", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequesterId(1)).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> result = itemRequestService.getAllRequesterItemRequests(1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(0, result.get(0).getItems().size());
    }

    @Test
    void getAllRequesterItemRequest_EmptyRequests() {
        User user = new User(1, "user", "user@mail.ru");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequesterId(1)).thenReturn(new ArrayList<>());

        List<ItemRequestDto> result = itemRequestService.getAllRequesterItemRequests(1);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getItemRequestById_EmptyItems() {
        User user = new User(1, "user", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(itemRequest));

        ItemRequestDto result = itemRequestService.getItemRequestById(1, 1);

        Assertions.assertEquals(0, result.getItems().size());
    }

    @Test
    void getItemRequestById_NotEmptyItems() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        Item item = new Item(1, "item", "description", true, owner, itemRequest);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(List.of(itemDtoForRequest));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(1)).thenReturn(List.of(item));

        ItemRequestDto result = itemRequestService.getItemRequestById(1, 1);

        Assertions.assertEquals(1, result.getItems().get(0).getId());
    }

    @Test
    void getItemRequestById_ThrowsRequestNotFoundException() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        Item item = new Item(1, "item", "description", true, owner, itemRequest);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(List.of(itemDtoForRequest));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(2)).thenReturn(Optional.empty());

        Assertions.assertThrows(RequestNotFoundException.class, () -> itemRequestService.getItemRequestById(1, 2));
    }

    @Test
    void getAllRequesterItemRequestsPagination_NotEmptyRequestsAndItems() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(2, "owner", "owner@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        Item item = new Item(1, "item", "description", true, owner, itemRequest);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.toItemDtoForRequest(item);
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(List.of(itemDtoForRequest));
        PageRequest page = PageRequest.of(0, 1);

        when(itemRequestRepository.findAllByRequesterIdNot(1, page)).thenReturn(new PageImpl<>(List.of(itemRequest)));
        when(itemRepository.findAllByRequestId(1)).thenReturn(List.of(item));

        List<ItemRequestDto> result = itemRequestService.getAllRequesterItemRequestsPagination(1, 0, 1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(1, result.get(0).getItems().get(0).getId());
    }

    @Test
    void getAllRequesterItemRequestsPagination_NotEmptyRequestsAndEmptyItems() {
        User user = new User(1, "user", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1, "description", user, LocalDateTime.now());
        PageRequest page = PageRequest.of(0, 1);

        when(itemRequestRepository.findAllByRequesterIdNot(1, page)).thenReturn(new PageImpl<>(List.of(itemRequest)));

        List<ItemRequestDto> result = itemRequestService.getAllRequesterItemRequestsPagination(1, 0, 1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(0, result.get(0).getItems().size());
    }

    @Test
    void getAllRequesterItemRequestsPagination_EmptyRequests() {
        PageRequest page = PageRequest.of(0, 1);

        when(itemRequestRepository.findAllByRequesterIdNot(1, page)).thenReturn(new PageImpl<>(new ArrayList<>()));

        List<ItemRequestDto> result = itemRequestService.getAllRequesterItemRequestsPagination(1, 0, 1);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAllRequesterItemRequestsPagination_ThrowsPaginationException() {

        Assertions.assertThrows(PaginationException.class, () -> itemRequestService.getAllRequesterItemRequestsPagination(1, -1, 1));
    }
}
