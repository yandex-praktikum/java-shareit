package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.Date;

@Getter
@Setter
public class ItemRequest {
    private Integer id;
    private String description;
    private User requestor;
    private Date created;

}
