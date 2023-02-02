package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.utilities.PaginationConverter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final ItemRequestRepository repository;
    private final ItemRequestMapper mapper;
    private final UserRepository userRepository;
    private final PaginationConverter paginationConverter;

    @Override
    public List<ItemRequestDtoResponse> getRequestByOwnerId(Long ownerId) {
        User user = userRepository.getUserById(ownerId);
        if (user == null) {
            throw new UserNotFoundException("can't find user with id " + ownerId);
        }
        List<ItemRequest> requests = repository.findRequestByRequestor_Id(ownerId);
        log.info("requests returned by owner id " + ownerId);
        return requests.stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoResponse> getAllRequests(Long userId, Integer from, Integer size) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("can't find user with id " + userId);
        }
        Pageable pageable = paginationConverter.convert(from, size, "created");
        List<ItemRequest> requests = repository.findAllRequests(userId, pageable);
        log.info("all requests returned by pagination " + pageable);
        return requests.stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoResponse getRequestById(Long requestId, Long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("can't find user with id " + userId);
        }
        ItemRequest request = repository.findRequestById(requestId);
        if (request == null) {
            throw new ItemRequestNotFoundException("can't find request by id " + requestId);
        }
        log.info("request returned by id " + requestId);
        return map(request);
    }


    @Transactional
    @Override
    public ItemRequestDtoResponse addRequest(ItemRequestDto requestDto, Long ownerId) {
        User user = userRepository.getUserById(ownerId);
        if (user == null) {
            throw new UserNotFoundException("can't find user with id " + ownerId);
        }
        requestDto.setRequestorId(ownerId);
        ItemRequest request = mapper.toRequest(requestDto, user);
        request = repository.save(request);
        log.info("request added by user with id " + ownerId);
        return mapper.toRequestDtoForResponse(request);
    }

    @Override
    public ItemRequestDtoResponse map(ItemRequest request) {
        List<ItemDto> items = itemRepository.findItemsByRequestId(request.getId()).stream()
                .map(ItemMapper::createDto).collect(Collectors.toList());
        ItemRequestDtoResponse response = mapper.toRequestDtoForResponse(request);
        response.setItems(items);
        return response;
    }
}
