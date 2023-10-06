package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
public class ItemRequest {
    private int id;
    private String description;
    private int requestor;
    private LocalDateTime created;

}
