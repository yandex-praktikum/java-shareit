package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.IncorrectPageParametrException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.mockito.ArgumentMatchers.any;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RequestServiceUnitTest {
    ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
    UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);

    private final ItemRequestService requestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository, mockItemRepository);

    @Test
    public void shouldSuccessCreate() {
        User requestor = new User(1, "sss@email.ru", "Sasha");
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.of(requestor));

        ItemRequestDto itemRequestDto = new ItemRequestDto(null, "коньки для катания", null, new Date(), null);

        ItemRequestDto newItemRequestDto = requestService.addItemRequest(1, itemRequestDto);

        Assertions.assertNotNull(newItemRequestDto);
        Assertions.assertEquals(newItemRequestDto.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    public void shouldReturnSortedItemRequestList() {
        User requestor = new User(1, "sss@email.ru", "Sasha");
        ItemRequest request1 = new ItemRequest(1, "костюм клоуна", requestor, getDate("2023-01-15"));
        ItemRequest request2 = new ItemRequest(2, "Платье ретро", requestor, getDate("2023-03-15"));
        ItemRequest request3 = new ItemRequest(3, "Костюм микки-маус", requestor, getDate("2023-03-01"));

        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(requestor));
        Mockito.when(mockItemRequestRepository.findByRequestor(any())).thenReturn(List.of(request1, request2, request3));

        List<ItemRequestDto> list = requestService.getItemRequests(1);

        Assertions.assertEquals(list.get(0).getCreated(), request1.getCreated());
        Assertions.assertEquals(list.get(1).getCreated(), request3.getCreated());
        Assertions.assertEquals(list.get(2).getCreated(), request2.getCreated());
    }

    @Test
    public void shouldSuccessGetItemRequests() {
        User requestor = new User(1, "sss@email.ru", "Sasha");
        ItemRequest request1 = new ItemRequest(1, "костюм клоуна", requestor, getDate("2023-01-15"));
        ItemRequest request2 = new ItemRequest(2, "Платье ретро", requestor, getDate("2023-03-15"));
        ItemRequest request3 = new ItemRequest(3, "Костюм микки-маус", requestor, getDate("2023-03-01"));
        Mockito.when(mockUserRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(requestor));
        Mockito.when(mockItemRequestRepository.findByRequestorLimits(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(request1, request2, request3));

        List<ItemRequestDto> list = requestService.getItemRequests(1, 1, 2);

        Assertions.assertEquals(list.get(0).getCreated(), request1.getCreated());
        Assertions.assertEquals(list.get(1).getCreated(), request3.getCreated());
        Assertions.assertEquals(list.get(2).getCreated(), request2.getCreated());
    }

    @Test
    public void shouldGetItemRequestsByPageParam() {
        User requestor = new User(1, "sss@email.ru", "Sasha");
        ItemRequest request1 = new ItemRequest(1, "костюм клоуна", requestor, getDate("2023-01-15"));
        ItemRequest request2 = new ItemRequest(2, "Платье ретро", requestor, getDate("2023-03-15"));
        ItemRequest request3 = new ItemRequest(3, "Костюм микки-маус", requestor, getDate("2023-03-01"));
        Mockito.when(mockItemRequestRepository.findAll()).thenReturn(List.of(request1, request2, request3));

        IncorrectPageParametrException exception = Assertions.assertThrows(IncorrectPageParametrException.class,
                () -> requestService.getItemRequests(requestor.getId(), -1, -1)
        );

        Assertions.assertNotNull(exception);

        List<ItemRequestDto> lsitItems = requestService.getItemRequests(null, null, null);
        Assertions.assertNotNull(lsitItems);
        Assertions.assertEquals(lsitItems.size(), 3);

    }

    @Test
    public void shouldReturnIncorrectParamException() {
        User requestor = new User(1, "eee@email.ru", "Eva");
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.of(requestor));
        IncorrectParameterException exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> requestService.getItemRequest(1, 1)
        );

        Assertions.assertEquals("Запрос не найден", exception.getParameter());
    }

    @Test
    public void shouldSuccessGetItemRequest() {
        User requestor = new User(1, "eee@email.ru", "Eva");
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.of(requestor));
        ItemRequest request = new ItemRequest(1, "костюм клоуна", requestor, getDate("2023-01-15"));
        Mockito.when(mockItemRequestRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(request));
        Mockito.when(mockItemRepository.findByRequestId(Mockito.anyInt())).thenReturn(new ArrayList<>());

        ItemRequestDto itemRequestDto = requestService.getItemRequest(1, 1);
        Assertions.assertEquals(itemRequestDto.getDescription(), request.getDescription());

    }

    @Test
    public void shouldMapToItemRequest() {
        User requestor = new User(1, "sss@email.ru", "Sasha");
        UserDto requestorDto = UserMapper.toUserDto(requestor);
        ItemRequestDto itemRequestDto = new ItemRequestDto(null, "коньки для катания", requestorDto, new Date(), null);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);

        Assertions.assertNotNull(itemRequest);
        Assertions.assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
        Assertions.assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
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
