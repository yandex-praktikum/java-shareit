package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.utilities.Constants;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @GetMapping
    public List<ItemRequestDtoResponse> getRequestsByOwnerId(@RequestHeader(Constants.USER_ID_HEADER) Long ownerId) {
        return requestService.getRequestByOwnerId(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoResponse> getRequestsWithPagination(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                                                   @RequestParam(required = false) Integer from,
                                                                   @RequestParam(required = false) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoResponse getRequestById(@PathVariable Long requestId,
                                                @RequestHeader(Constants.USER_ID_HEADER) Long userId) {
        return requestService.getRequestById(requestId, userId);
    }

    @PostMapping
    public ItemRequestDtoResponse add(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                           @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.addRequest(itemRequestDto, userId);
    }
}