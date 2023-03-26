package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Constants;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto create(@Valid @RequestBody ItemRequestCreateRequest itemRequestCreateRequest,
                                 @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") long requestAuthorId)
            throws ValidationException {
        log.info("Create item request, owner {}: " + itemRequestCreateRequest.toString(), requestAuthorId);
        if (requestAuthorId <= 0) {
            throw new ValidationException("Указан ошибочный id автора запроса");
        }

        return ItemRequestDtoMapper.toItemRequestDto(itemRequestService.create(ItemRequestDtoMapper.toItemRequest(itemRequestCreateRequest), requestAuthorId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getOwn(@RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") long requestAuthorId) {
        log.info("Get own item requests authorId {}", requestAuthorId);
        return ItemRequestDtoMapper.toItemRequestDtoList(itemRequestService.getOwnItemRequests(requestAuthorId));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getAll(@RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") long requestAuthorId) {
        log.info("Get all item requests from {} size {} requestAuthorId {}", from, size, requestAuthorId);
        return ItemRequestDtoMapper.toItemRequestDtoList(itemRequestService.getAll(from, size, requestAuthorId));
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getById(@PathVariable int requestId,
                                  @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") long userId) {
        log.info("Get requestId {} for userId {}", requestId, userId);
        return ItemRequestDtoMapper.toItemRequestDto(itemRequestService.getById(requestId, userId));
    }
}