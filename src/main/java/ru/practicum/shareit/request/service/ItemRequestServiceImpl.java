package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {


    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequest saveItemRequest(long userId, ItemRequestDto itemRequestDto) {
             if (userRepository.findById(userId)==null){
            throw new NotDataException("нет такого пользователя");
        }
             if (itemRequestDto.getDescription()==null){
                 throw new NotDataException("description не может быть пустым");
             }
             if (itemRequestDto.getDescription().isEmpty() || itemRequestDto.getDescription().isBlank() ){
                 throw new NotDataException("description не может быть пустым");
             }
       ItemRequest itemRequest = RequestMapper.mapToRequest(itemRequestDto);
        itemRequest.setRequestor(userId);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest=itemRequestRepository.save(itemRequest);
        log.info("создан запрос id " + itemRequest.getId());
                return itemRequest;
    }

    @Override
    public ItemRequest updateItemRequest(long userId, long itemId ,ItemRequestDto itemRequestDto) {
        if (itemRequestRepository.findById(itemId)==null){
            throw new NotDataException("нет запроса вещи с таким id "+ itemId);
        }
        if (findIdItemRequestById(itemId).getRequestor()!=userId){
            throw new NotDataException("вы не являетесь владельцем запроса вещи");
        }
        ItemRequest itemRequest = RequestMapper.mapToRequest(itemRequestDto);
       itemRequest.setId(itemId);
       itemRequest.setCreated(LocalDateTime.now());
       log.info("обновление вещи, передан id "+itemId);
      itemRequest = itemRequestRepository.update(itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequestDto findIdItemRequestById(long id) {
        log.info("получение запроса по id");
        return RequestMapper.mapToRequestDto(itemRequestRepository.findById(id));
    }

    @Override
    public ItemRequestDto findItemRequestByDescription(String description) {
                log.info("получение запроса по имени вещи");
        Optional<ItemRequest> optionalItemRequestDto = itemRequestRepository.findByDescription(description.toLowerCase());
        if ( optionalItemRequestDto.isEmpty() ){
            throw new NotDataException("нет такого запроса вещи");
        }
                return RequestMapper.mapToRequestDto(optionalItemRequestDto.get());
    }

    @Override
    public Collection<ItemRequestDto> findAll() {
        log.info("получение списка запросов вещей");
        return itemRequestRepository.findAll().stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long userId, long id) {
        if (itemRequestRepository.findById(id)==null){
            throw new NotDataException("нет вещи с таким id "+ id);
        }
        if (itemRequestRepository.findById(id).getRequestor() !=userId){
            throw new ValidationException("вы не являетесь владельцем вещи");
        }
        itemRequestRepository.delete(id);
        log.info("удаление запроса вещи, передан id "+id);


    }
}
