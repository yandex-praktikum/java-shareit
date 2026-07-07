package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private Long itemId;
    private String itemName;
    private Long ownerId;
}