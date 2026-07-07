package ru.practicum.shareit.request.model;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestItemRequestDto {
    private String description;
}