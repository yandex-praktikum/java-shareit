package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto create(Long userId, ItemRequestCreateDto requestDto) {

        User requester = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Пользователь с id = " + userId + " не найден"));

        ItemRequest request = ItemRequestMapper.toEntity(requestDto);

        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toDto(
                itemRequestRepository.save(request)
        );
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {

        List<ItemRequest> requests =
                itemRequestRepository.findAllByRequesterId(userId);

        return ItemRequestMapper.toDtoList(requests);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {

        List<ItemRequest> requests =
                itemRequestRepository.findAllExceptRequester(userId);

        return ItemRequestMapper.toDtoList(requests);
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId) {

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Запрос с id = " + requestId + " не найден"));

        return ItemRequestMapper.toDto(request);
    }
}