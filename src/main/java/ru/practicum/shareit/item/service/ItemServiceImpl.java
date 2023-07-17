package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundRecordInBD;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.validation.ValidationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ValidationService validationService;

    private final ItemMapper mapper;

    public ItemServiceImpl(@Qualifier("InMemory") ItemRepository itemRepository,
                           ValidationService validationService, ItemMapper mapper) {
        this.itemRepository = itemRepository;
        this.validationService = validationService;
        this.mapper = mapper;
    }


    /**
     * Обновить вещь в БД. Редактирование вещи. Эндпойнт PATCH /items/{itemId}.
     * <p>Изменить можно название, описание и статус доступа к аренде.</p>
     * <p>Редактировать вещь может только её владелец.</p>
     * @param itemDto вещь.
     * @param ownerId ID хозяина вещи.
     * @param itemId  ID обновляемой вещи.
     * @return обновлённая вещь.
     */
    @Override
    public ItemDto updateInStorage(ItemDto itemDto, Long ownerId, Long itemId) {
        if (ownerId == null) {
            String message = "Для обновления надо передать ID хозяина вещи.";
            log.info("Error 400. " + message);
            throw new ValidateException(message);
        }
        itemDto.setOwnerId(ownerId);
        itemDto.setId(itemId);
        Item item = mapper.mapToModel(itemDto);
        Item itemFromDB = validationService.checkExistItemInDB(item.getId());

        if (!validationService.isOwnerItem(itemFromDB, ownerId)) {
            String message = String.format("Вещь %s не принадлежит пользователю с ID = %d.", itemFromDB.getName(), ownerId);
            throw new NotFoundRecordInBD("Error 404. " + message);
        }
        validationService.checkExistUserInDB(ownerId);
        boolean[] isUpdateFields = validationService.checkFieldsForUpdate(item);
        ItemDto result = mapper.mapToDto(itemRepository.updateInStorage(item, isUpdateFields));
        log.info("Была обновлена вещь {}, id = {}", result.getName(), result.getId());
        return result;
    }

    /**
     * Добавить вещь в репозиторий.
     * @param itemDto    добавленная вещь.
     * @param ownerId ID владельца вещи.
     * @return добавленная вещь.
     */
    @Override
    public ItemDto add(ItemDto itemDto, Long ownerId) {
        itemDto.setOwnerId(ownerId);
        Item item = mapper.mapToModel(itemDto);
        validationService.validateItemFields(item);
        validationService.checkMissingItemInDB(item.getId());
        validationService.checkExistUserInDB(item.getOwnerId());

        ItemDto result = mapper.mapToDto(itemRepository.add(item));
        log.info("Выполнено добавление новой вещи в БД: " + result + ".");

        return result;
    }

    /**
     * Получить список вещей.
     * @return список вещей.
     */
    @Override
    public List<ItemDto> getAllItems(Long userId) {

        List<ItemDto> result = itemRepository.getAllItems(userId).stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(mapper::mapToDto).collect(Collectors.toList());
        log.info("Выдан ответ на запрос вещей пользователя с ID = " + userId + ".");
        return result;
    }

    /**
     * Получить вещь по ID.
     * @param itemId ID вещи.
     * @return запрашиваемая вещь.
     */
    @Override
    public Item getItemById(Long itemId) {
        Item result;
        validationService.checkExistItemInDB(itemId);
        result = itemRepository.getItemById(itemId);
        return result;
    }

    /**
     * Есть ли запрашиваемая вещь с ID в хранилище.
     * @param itemId ID запрашиваемой вещи.
     * @return запрашиваемая вещь.
     */
    @Override
    public Boolean isExcludeItemById(Long itemId) {
        return itemRepository.isExcludeItemById(itemId);
    }

    /**
     * Удалить вещь с ID из хранилища.
     * @param itemId ID удаляемой вещи.
     */
    @Override
    public Item removeItemById(Long itemId) {
        Item item = validationService.checkExistItemInDB(itemId);
        itemRepository.removeItemById(itemId);
        return item;
    }

    /**
     * Поиск вещей по тексту.
     * @param text текст.
     * @return список вещей.
     */
    @Override
    public List<ItemDto> searchItemsByText(String text) {

        if (text == null || text.isBlank()) {
            String message = String.format("По запросу поиска '%s' передан пустой список.", text);
            log.info(message);
            return Collections.emptyList();
        }

        List<ItemDto> list = new ArrayList<>();
        for (Item item : itemRepository.searchItemsByText(text)) {
            ItemDto itemDto = mapper.mapToDto(item);
            list.add(itemDto);
        }
        String message = String.format("По запросу поиска '%s' передан список: %s", text, list);
        log.info(message);


        return list;
    }
}
