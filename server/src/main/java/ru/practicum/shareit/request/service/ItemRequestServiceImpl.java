package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.PageCreatorUtil;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, int requesterId) {
        User requester = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requester);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllRequesterItemRequests(int requesterId) {
        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterId(requesterId);
        if (!itemRequests.isEmpty()) {
            List<ItemRequestDto> itemRequestDtos = ItemRequestMapper.listToItemRequestDto(itemRequests);
            for (ItemRequestDto itemRequestDto : itemRequestDtos) {
                List<Item> requestItems = itemRepository.findAllByRequestId(itemRequestDto.getId());
                if (!requestItems.isEmpty()) {
                    List<ItemDtoForRequest> itemDtoForRequests = ItemMapper.listToItemDtoForRequest(requestItems);
                    itemRequestDto.setItems(itemDtoForRequests);
                } else {
                    itemRequestDto.setItems(new ArrayList<>());
                }
            }
            return itemRequestDtos;
        }
        return new ArrayList<>();
    }

    @Override
    public ItemRequestDto getItemRequestById(int requesterId, int requestId) {
        User user = userRepository.findById(requesterId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException("Такого запроса не существует"));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        List<Item> requestItems = itemRepository.findAllByRequestId(itemRequestDto.getId());
        if (!requestItems.isEmpty()) {
            List<ItemDtoForRequest> itemDtoForRequests = ItemMapper.listToItemDtoForRequest(requestItems);
            itemRequestDto.setItems(itemDtoForRequests);
        } else {
            itemRequestDto.setItems(new ArrayList<>());
        }
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getAllRequesterItemRequestsPagination(int requesterId, Integer from, Integer size) {
        PageRequest page = PageCreatorUtil.createPage(from, size);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNot(requesterId, page).getContent();
        if (!itemRequests.isEmpty()) {
            List<ItemRequestDto> itemRequestDtos = ItemRequestMapper.listToItemRequestDto(itemRequests);
            for (ItemRequestDto itemRequestDto : itemRequestDtos) {
                List<Item> requestItems = itemRepository.findAllByRequestId(itemRequestDto.getId());
                if (!requestItems.isEmpty()) {
                    List<ItemDtoForRequest> itemDtoForRequests = ItemMapper.listToItemDtoForRequest(requestItems);
                    itemRequestDto.setItems(itemDtoForRequests);
                } else {
                    itemRequestDto.setItems(new ArrayList<>());
                }
            }
            return itemRequestDtos;
        }
        return new ArrayList<>();
    }
}
