package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.practicum.shareit.user.User;

@Data
@EqualsAndHashCode
@ToString
public class Item {

    private int id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private String request;
}
