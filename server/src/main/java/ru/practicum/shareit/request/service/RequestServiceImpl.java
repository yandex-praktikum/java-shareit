package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.dto.FullRequestDto;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final RequestMapper requestMapper;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public RequestDto createRequest(RequestAddDto requestAddDto, Long userId) {
        log.info("Пользователь с id {} создает запрос", userId);
        RequestDto newRequest = RequestDto.builder()
                .description(requestAddDto.getDescription())
                .userId(userId)
                .created(LocalDateTime.now()).build();

        User user = checkUser(userId);
        ItemRequest entity = requestMapper.mapItemRequest(newRequest, user);
        return requestMapper.mapRequestDto(requestRepository.save(entity));
    }

    @Override
    public FullRequestDto getRequestById(Long requestId, Long userId) {
        log.info("Пользователь с id {} запрашивает информацию о запросе {}", userId, requestId);
        checkUser(userId);
        return requestMapper.mapFullRequestDto(checkRequest(requestId), getItems(requestId));
    }

    @Override
    public List<FullRequestDto> getRequestByUser(Long userId) {
        log.info("Запрос на получение списка запросов пользователя с  id {}", userId);

        List<ItemRequest> requests = requestRepository.findByRequester_IdOrderByCreatedDesc(userId);
        return  requests.stream()
                .map(request -> requestMapper.mapFullRequestDto(request, getItems(request.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAllRequests(Long userId) {
        log.info("Пользователь с id {} отправил запрос на получение списка запросов", userId);

        User user = checkUser(userId);
        List<ItemRequest> requests = requestRepository.findByNotUserId(user.getId());
        return requestMapper.mapRequestDto(requests);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя с id " + userId));
    }

    private ItemRequest checkRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти запрос с id " + requestId));
    }

    private List<ShortItemDto> getItems(Long requestId) {
        List<Item> itemsEntity = itemRepository.findByRequest_Id(requestId);
        return itemMapper.toShortItemDtoList(itemsEntity);
    }
}
