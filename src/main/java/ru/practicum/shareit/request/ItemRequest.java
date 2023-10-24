package ru.practicum.shareit.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@ToString
public class ItemRequest {

    private int id;
    private String description;
    private User requester;
    private LocalDateTime created;
}
