package ru.practicum.shareit.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private ItemRequestRepository repository;
    private UserService userService;
    private ItemRequestMapper mapper;


    @Override
    public ItemRequestDto create(ItemRequestCreatedDto requestDto, long requestorId) {
        ItemRequest request = mapper.toItemRequestFromCreateDto(requestDto);
        request.setRequestor(userService.getUserById(requestorId));
        return mapper.toItemRequestDto(repository.save(request));
    }

    @Override
    public List<ItemRequestDto> getAllByRequestorId(long requestorId) {
        userService.getUserById(requestorId);

        return repository.findAllByRequestorIdOrderByCreatedDesc(requestorId)
                .stream()
                .map(mapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("created"));

        return repository.findAllByRequestorIdNotOrderByCreatedDesc(userId, pageable)
                .stream()
                .map(mapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto get(long requestId, long userId) {
        userService.getUserById(userId);
        Optional<ItemRequest> request = repository.findById(requestId);

        if (request.isPresent()) {
            return mapper.toItemRequestDto(request.get());
        } else {
            throw new RequestNotFoundException("Запрос не найден");
        }
    }
}
