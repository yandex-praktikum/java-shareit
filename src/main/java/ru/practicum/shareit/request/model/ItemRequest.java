package ru.practicum.shareit.request.model;

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
public class ItemRequest {
    private Long Id;
    private String nameUserRequest;
    private String description;
    private LocalDate timeCreation;

}
