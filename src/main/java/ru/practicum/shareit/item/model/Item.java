package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Data
@Validated
public class Item {
    private int id;
    private int userId;
    private String name;
    private String description;
    private Boolean available;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && userId == item.userId && available == item.available && name.equals(item.name) && description.equals(item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, description, available);
    }
}
