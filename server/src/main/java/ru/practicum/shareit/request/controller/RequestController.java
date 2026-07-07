package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.answers.model.AnswerDto;
import ru.practicum.shareit.request.answers.repository.AnswerRepository;
import ru.practicum.shareit.request.mapper.RequestItemMapper;
import ru.practicum.shareit.request.model.*;
import ru.practicum.shareit.request.service.RequestItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestItemService requestItemService;
    private final RequestItemMapper mapper;
    private final AnswerRepository answerRepository;

    @PostMapping
    public RequestItemResponseDto createRequest(@RequestBody RequestItemRequestDto dto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId) {
        RequestItem entity = mapper.mapToRequestItem(dto);
        RequestItem created = requestItemService.createRequestItem(entity, userId);
        return mapper.mapToDto(created);
    }

    @GetMapping
    public List<RequestItemResponseDto> getMyRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestItemService.getAllRequestItemByOwner(userId).stream()
                .map(this::enrichWithAnswers)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<RequestItemResponseDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestItemService.getAllRequestItemAllUsers(userId).stream()
                .map(this::enrichWithAnswers)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public RequestItemResponseDto getRequestById(@PathVariable Long requestId) {
        return enrichWithAnswers(requestItemService.getRequestItemById(requestId));
    }

    private RequestItemResponseDto enrichWithAnswers(RequestItem requestItem) {
        RequestItemResponseDto dto = mapper.mapToDto(requestItem);

        List<AnswerDto> answers = answerRepository.findByRequestId(requestItem.getId()).stream()
                .map(answer -> AnswerDto.builder()
                        .id(answer.getId())
                        .itemId(answer.getItem().getId())
                        .itemName(answer.getItem().getName())
                        .ownerId(answer.getItem().getOwner().getId())
                        .build())
                .toList();

        dto.setAnswers(answers);
        return dto;
    }
}