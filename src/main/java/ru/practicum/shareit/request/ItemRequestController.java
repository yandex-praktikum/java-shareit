package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ItemRequest createItemRequest(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemRequestDto itemRequestDto) {
        log.info("создание запроса создания вещи");
        return itemRequestService.saveItemRequest(userId, itemRequestDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequest updateItemRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id,
                                  @RequestBody ItemRequestDto itemRequestDto) {
        log.info("обновление запроса создания вещи");
        return itemRequestService.updateItemRequest(userId, id, itemRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto findIdItemRequestById(@PathVariable("id") long id) {
        log.info("вывод запроса создания вещи id " + id);
        return itemRequestService.findIdItemRequestById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto findItemRequestByDescription(@RequestParam String item) {
        log.info("вывод запроса по названию вещи");
        return itemRequestService.findItemRequestByDescription(item);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ItemRequestDto> findAll() {
        log.info("вывод всех запросов создания вещи");
        return itemRequestService.findAll();
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long id) {
        log.info("удаление запроса создания вещи");
        itemRequestService.delete(userId, id);
    }
}
