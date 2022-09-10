package ru.practicum.shareit.requests.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.requests.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(Constants.USER_ID_HEADER) long requestorId,
                                 @RequestBody ItemRequestCreatedDto dto) {
        if (dto.getDescription() == null) {
            throw new BadRequestException("описание не найдено");
        }
        return requestService.create(dto, requestorId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUserId(@RequestHeader(Constants.USER_ID_HEADER) long requestorId) {
        return requestService.getAllByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "25") int size) {
        return requestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto get(@RequestHeader(Constants.USER_ID_HEADER) long userId,
                              @PathVariable long requestId) {
        return requestService.get(requestId, userId);
    }
}
