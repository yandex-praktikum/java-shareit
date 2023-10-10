package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
