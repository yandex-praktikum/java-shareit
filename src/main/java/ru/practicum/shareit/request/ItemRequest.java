package ru.practicum.shareit.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequest {
    private Long id;
    @NotBlank
    private String description;
    @Positive
    private Long requestor; // id пользователя, создавшего запрос
    private LocalDateTime created;
}
