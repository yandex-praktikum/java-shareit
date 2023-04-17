package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Setter
@Getter
@AllArgsConstructor
public class ItemRequestDto {
    private String requestor;
    private String description;
    private LocalDate created;
}
