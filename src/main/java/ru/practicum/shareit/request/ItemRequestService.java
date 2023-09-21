package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    public ItemRequestService(ItemRequestRepository itemRequestRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public ItemRequest add(Long userId, ItemRequest request) {
        userRepository.containsById(userId);
        return itemRequestRepository.add(userId, request);
    }

    public ItemRequestDto update(Long userId, ItemRequestDto requestDto, Long requestId) {
        userRepository.containsById(userId);
        return itemRequestRepository.update(userId, requestDto, requestId);
    }

    public List<ItemRequest> getAllForUser(Long userId) {
        userRepository.containsById(userId);
        return itemRequestRepository.getAllForUser(userId);
    }

    public ItemRequestDto getOne(Long userId, Long requestId) {
        userRepository.containsById(userId);
        return itemRequestRepository.getOneWithOutOwner(requestId);
    }

    public List<ItemRequestDto> search(Long userId, String text) {
        userRepository.containsById(userId);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRequestRepository.search(text.toLowerCase());
    }

    public void delete(Long userId, Long requestId) {
        userRepository.containsById(userId);
        itemRequestRepository.delete(userId, requestId);
    }
}