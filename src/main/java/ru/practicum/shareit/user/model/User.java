package ru.practicum.shareit.user.model;

import lombok.*;

@Data
@AllArgsConstructor
public class User {
    private final Long id;
    private String name;
    private String email;
}
