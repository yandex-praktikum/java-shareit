package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {
    final UserRepository userRepository;
    final RequestRepository requestRepository;

    @Override
    public RequestDto create(RequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserServiceImpl.exceptionFormat(userId));

        Request request = RequestMapper.convertToRequest(requestDto);
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        request = requestRepository.save(request);

        log.info("Запрос  {}  пользователя id={} сохранён",
                request,
                userId);
        return RequestMapper.convertToRequestDto(request);
    }

    @Override
    public List<RequestDto> getAllUserRequest(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> UserServiceImpl.exceptionFormat(userId));

        log.info("Вещи пользователя с id = {}", userId);
        return requestRepository.searchAllByRequestor_IdOrderByCreatedDesc(userId)
                .stream()
                .map(RequestMapper::convertToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto get(Long requestId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> UserServiceImpl.exceptionFormat(userId));

        return RequestMapper.convertToRequestDto(
                requestRepository.findById(requestId)
                        .orElseThrow(() -> exceptionFormat(requestId)));
    }

    @Override
    public List<RequestDto> getAllNoUserRequest(Long userId, Integer from, Integer size) {


        log.info("Вещи других пользователей");

        return requestRepository.searchAllByRequestor_IdNot(userId,
                        new OffsetBasedPageRequest(from,
                                size,
                                Sort.by("created").descending()))
                .stream()
                .map(RequestMapper::convertToRequestDto)
                .collect(Collectors.toList());
    }

    public static NotFoundException exceptionFormat(Long id) {
        return new NotFoundException(String.format("Зпрос с id = %s, не найден", id));
    }

}
