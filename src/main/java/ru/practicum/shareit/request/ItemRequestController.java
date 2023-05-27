package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utilits.Variables;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final String pathId = "/{id}";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(value = Variables.USER_ID) Integer userId,
                                 @RequestBody @NotNull ItemRequestDto itemRequestDto) {
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getRequests(@RequestHeader(value = Variables.USER_ID) Integer userId) {
        return itemRequestService.getItemRequests(userId);
    }

    @GetMapping(pathId)
    public ItemRequestDto getRequest(@RequestHeader(value = Variables.USER_ID) Integer userId,
                                     @PathVariable Integer id) {
        return itemRequestService.getItemRequest(userId, id);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequests(@RequestHeader(value = Variables.USER_ID) Integer owner,
                                            @RequestParam(required = false, name = "from") Integer from,
                                            @RequestParam(required = false, name = "size") Integer size) {
        return itemRequestService.getItemRequests(owner, from, size);
    }
}
