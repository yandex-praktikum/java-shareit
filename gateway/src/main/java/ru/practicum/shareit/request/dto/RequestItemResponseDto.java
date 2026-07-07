package ru.practicum.shareit.request.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestItemResponseDto {
    private Long id;
    private String description;
    private Long requestorId;
    private String created;
    private List<AnswerDto> answers;
}