package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
public class User {
    Long id;        //ID пользователя.
    String name;    //Имя пользователя.
    String email;   //Электронная почта.
}

