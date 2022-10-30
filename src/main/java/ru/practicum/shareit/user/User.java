package ru.practicum.shareit.user;

import lombok.*;

@Data
@AllArgsConstructor
public class User {
    private final Long id;
    private String name;
    private String email;
}
