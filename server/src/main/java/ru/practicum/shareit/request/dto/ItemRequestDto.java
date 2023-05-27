package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.Date;

@Getter
@Setter
public class ItemRequestDto {
    private String description;
    private User requestor;
    private Date created;
}
