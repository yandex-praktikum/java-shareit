package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class User {

    private long id;        //уникальный идентификатор пользователя

    private String name;    //имя или логин пользователя

    @Email
    private String email;   //адрес электронной почты
    //(два пользователя не могут иметь одинаковый адрес электронной почты).
}