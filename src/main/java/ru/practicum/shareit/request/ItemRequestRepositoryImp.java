package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.MainData;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRequestRepositoryImp implements ItemRequestRepository {
    private final List<ItemRequest> requests;
    private final List<Item> items;
    private Long globalId = 1L;

    public ItemRequestRepositoryImp(MainData mainData) {
        this.requests = mainData.getRequests();
        items = mainData.getItems();
    }

    @Override
    public ItemRequest add(Long userId, ItemRequest request) {
        request.setId(globalId);
        globalId++;
        request.setRequesterId(userId);
        requests.add(request);
        return request;
    }

    @Override
    public void containsById(Long requestId) {
        requests.stream().filter(request -> request.getId().equals(requestId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("request по id - " + requestId + " не найден"));
    }

    @Override
    public void containsSameOwner(Long userId, Long requestId) {
        Long ownerId = getRequest(requestId).getRequesterId();
        if (!ownerId.equals(userId)) {
            throw new EntityNotFoundException("user - " + userId + " не владеет request - " + requestId);
        }
    }

    @Override
    public ItemRequestDto update(Long userId,ItemRequestDto requestDto, Long requestId) {
        containsSameOwner(userId, requestId);
        ItemRequest oldRequest = getRequest(requestId);
        if (requestDto.getDescription() != null) {
            oldRequest.setDescription(requestDto.getDescription());
        }
        if (requestDto.getResolved() != null) {
            oldRequest.setResolved(requestDto.getResolved());
        }
        return toDto(oldRequest);
    }

    @Override
    public List<ItemRequest> getAllForUser(Long userId) {
        return requests.stream().filter(request -> request.getRequesterId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getOneWithOutOwner(Long requestId) {
        containsById(requestId);
        return toDto(getRequest(requestId));
    }

    @Override
    public List<ItemRequestDto> search(String text) {
        return requests.stream()
                .filter(request -> request.getDescription().toLowerCase().contains(text))
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long requestId) {
        containsSameOwner(userId, requestId);
        for (ItemRequest request : requests) {
            if (request.getId().equals(requestId)) {
                for (Item item : items) {
                   if (requestId.equals(item.getRequestId())) {
                       item.setRequestId(null);
                   }
                }
                requests.remove(request);
            }
        }
    }

    private ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setDescription(request.getDescription());
        requestDto.setResolved(request.getResolved());
        return requestDto;
    }

    private ItemRequest getRequest(Long requestId) {
        return requests.stream().filter(request -> request.getId().equals(requestId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("request по id - " + requestId + " не найден"));
    }
}
