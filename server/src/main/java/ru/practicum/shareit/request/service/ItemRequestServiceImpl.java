package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.base.service.BaseInDbService;
import ru.practicum.shareit.item.dto.item.ItemInItemRequestDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class ItemRequestServiceImpl extends BaseInDbService<ItemRequest, ItemRequestRepository> implements ItemRequestService {
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository repository,
                                  UserService userService, ItemRepository itemRepository) {
        super(repository, ItemRequest.class);
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto create(Long userId, RequestItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.fromDto(itemRequestDto);
        User user = userService.findById(userId);

        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        return toDto(super.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> findAllRequestsOfUser(Long userId) {
        Collection<ItemRequestDto> allRequestsOfUser = repository.findAllRequestsOfUser(userId);
        allRequestsOfUser.forEach(this::fillResponses);

        return allRequestsOfUser;
    }

    @Override
    public Collection<ItemRequestDto> findAll(Long userId) {
        return super.repository.findAll(userId);
    }

    @Override
    public ItemRequestDto findItemRequestBy(Long id) {
        ItemRequestDto foundItemRequest = toDto(super.findById(id));
        fillResponses(foundItemRequest);
        return foundItemRequest;
    }

    private static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestMapper.toDto(itemRequest);
    }

    private void fillResponses(ItemRequestDto itemRequestDto) {
        List<ItemInItemRequestDto> allItemByRequestId = itemRepository.findAllItemByRequestId(itemRequestDto.getId());
        log.debug("Все Item в ответ на запрос ItemRequest{id={}} возвращены ::/ return [size={}]",
                itemRequestDto.getId(), allItemByRequestId.size());

        itemRequestDto.setResponses(new HashSet<>(allItemByRequestId));
    }
}
