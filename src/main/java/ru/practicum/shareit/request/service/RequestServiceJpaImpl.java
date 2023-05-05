package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.RequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestJpaRepository;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.validator.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceJpaImpl implements RequestService {

    private final RequestJpaRepository requestRepository;

    private final ItemJpaRepository itemRepository;

    private final UserJpaRepository userRepository;

    private final Validator validator;

    @Override
    public ItemRequestDto createRequest(int userId, ItemRequest itemRequest) {
        if (validator.validateUser(userId, userRepository)) {
            if (!itemRequest.getDescription().isBlank()) {
                itemRequest.setUser(userRepository.findById(userId).get());
                itemRequest.setCreated(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.
                        ofPattern("yyyy-MM-dd'T'HH:mm"))));
                return RequestMapper.itemRequestToItemRequestDto(requestRepository.save(itemRequest));
            } else {
                log.info("Ошибка валидации запроса на вещь");
                throw new ValidationException();
            }
        } else {
            log.info("Пользователь не найден");
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<ItemRequestDto> getAllUserRequests(int userId) {
        if (validator.validateUser(userId, userRepository)) {
            List<ItemRequest> userRequests = requestRepository.findByUserId(userId);
            List<ItemRequestDto> userRequestDtos = new ArrayList<>();
            if (userRequests.isEmpty()) {
                return userRequestDtos;
            }
            for (ItemRequest itemRequest : userRequests) {
                List<ItemDto> items = ItemMapper.itemsToItemDtos(itemRepository.findByRequestId(itemRequest.getId()));
                userRequestDtos.add(RequestMapper.itemRequestToItemRequestDto(itemRequest, items));
            }
            return userRequestDtos;
        } else {
            log.info("Пользователь не найден");
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<ItemRequestDto> getAllRequests(int userId, int from, int size) {
        if ((from >= 0) && (size > 0)) {
            Pageable page = PageRequest.of(0, size + from, Sort.by(Sort.Direction.DESC, "id"));
            List<ItemRequest> requests = requestRepository.findAllRequests(userId, page);
            if (requests.size() > size) {
                requests.subList(0, size);
            }
            List<ItemRequestDto> requestDtos = new ArrayList<>();
            if (requests.isEmpty()) {
                return requestDtos;
            }
            for (ItemRequest itemRequest : requests) {
                List<ItemDto> itemDtos = ItemMapper.itemsToItemDtos(itemRepository.
                        findByRequestId(itemRequest.getId()));
                requestDtos.add(RequestMapper.itemRequestToItemRequestDto(itemRequest, itemDtos));
            }
            if (requestDtos.size() > size) {
                return requestDtos.subList(from, requestDtos.size());
            } else {
                return requestDtos;
            }
        } else {
            log.info("Параметры from и size не могут быть меньше 0");
            throw new ValidationException();
        }
    }

    @Override
    public ItemRequestDto findRequestById(int userId, int requestId) {
        if (validator.validateUser(userId, userRepository)) {
            return RequestMapper.itemRequestToItemRequestDto(
                    requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new),
                    ItemMapper.itemsToItemDtos(itemRepository.findByRequestId(requestId))
            );
        } else {
            log.info("Пользователь не найден");
            throw new UserNotFoundException();
        }
    }
}
