package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Объект пользователя
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private long id;
    private String email;
    private String name;
}
