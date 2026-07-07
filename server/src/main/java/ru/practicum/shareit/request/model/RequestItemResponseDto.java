package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.answers.model.AnswerDto;

import java.util.List;

@Getter
@Setter
@Builder
public class RequestItemResponseDto {
    private Long id;
    private String description;
    private Long requestorId;
    private String created;
    private List<AnswerDto> answers;
}