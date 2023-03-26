package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Constants;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    /**
     * сохранить новую вещь в хранилище, присвоить уникальный id
     *
     * @param itemCreateRequest заполненный валидированный объект ItemCreateRequest
     * @param ownerId           id пользователя, который будет указан владельцем вещи
     * @return заполненный объект ItemDto
     * @throws ValidationException если указан ошибочный id пользователя-владельца (<=0)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@Valid @RequestBody ItemCreateRequest itemCreateRequest,
                          @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId)
            throws ValidationException {
        log.info("Create item, owner {}: " + itemCreateRequest.toString(), ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("Указан ошибочный id владельца");
        }

        return ItemDtoMapper.toItemDto(itemService.create(itemCreateRequest, ownerId));
    }

    /**
     * изменить данные вещи с указанным id
     *
     * @param itemId  id Вещи
     * @param itemDto заполненный объект ItemDto
     * @param ownerId id пользователя - владельца вещи
     * @return заполненный объект ItemDto
     */
    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@PathVariable int itemId,
                          @Valid @RequestBody ItemDto itemDto,
                          @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId)
            throws ValidationException {
        log.info("Update item {}, ownerId {}: " + itemDto, itemId, ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("Указан ошибочный id владельца");
        }

        itemDto.setId(itemId);
        Item item = ItemDtoMapper.toItem(itemDto);

        return ItemDtoMapper.toItemDto(itemService.update(item, ownerId));
    }

    /**
     * получить список вещей, принадлежащих пользователю с указанным id
     *
     * @param ownerId id пользователя
     * @return список объектов типа ItemDto
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getOwnedItemsList(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "20") int size,
                                           @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Get owned items list, ownerId {}", ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("Указан ошибочный id владельца");
        }

        return ItemDtoMapper.toItemDtoList(itemService.getOwnedItemsList(ownerId, from, size));
    }

    /**
     * получить данные вещи по id
     *
     * @param itemId id вещи
     * @return объект типа ItemDto
     */
    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto get(@PathVariable int itemId,
                       @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Get itemId {}", itemId);
        return ItemDtoMapper.toItemDto(itemService.getById(itemId, ownerId));
    }

    /**
     * поиск строки в названии или описании вещей
     *
     * @param text строка поиска
     * @return список объектов Item, которые содержат искомую строку в названиях или описаниях
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "20") int size,
                                @RequestParam(defaultValue = "") String text) {
        log.info("Search text '{}'", text);
        if (!text.isBlank()) {
            return ItemDtoMapper.toItemDtoList(itemService.search(text, from, size));
        } else {
            return List.of();
        }
    }

    /**
     * удалить вещь с указанным id из хранилища
     *
     * @param itemId  id вещи
     * @param ownerId id пользователя - владельца вещи
     */
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int itemId,
                       @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int ownerId) {
        log.info("Delete itemId {}, ownerId {}", itemId, ownerId);
        if (ownerId <= 0) {
            throw new ValidationException("Указан ошибочный id владельца");
        }

        itemService.delete(itemId, ownerId);
    }

    /**
     * оставить отзыв к вещи если ранее её бронировал
     *
     * @param commentDto данные отзыва
     * @param itemId     id вещи
     * @param authorId   id автора
     * @return объект CommentDto
     * @throws ValidationException если передан ошибочный параметр authorId
     */
    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable int itemId,
                                    @RequestHeader(value = Constants.X_HEADER_NAME, defaultValue = "0") int authorId)
            throws ValidationException {
        log.info("Create comment for item {}, author {}: " + commentDto.toString(), itemId, authorId);
        if (authorId <= 0) {
            throw new ValidationException("Указан ошибочный id автора комментария");
        }

        return CommentDtoMapper.toCommentDto(itemService.createComment(commentDto.getText(), itemId, authorId));
    }
}