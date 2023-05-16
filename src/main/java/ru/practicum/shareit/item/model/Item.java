package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.review.Review;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
/**
 * TODO Sprint add-controllers.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "create")
@AllArgsConstructor
public class Item {
    @EqualsAndHashCode.Exclude
    Long id;
    Long ownerId;
    @NotNull(message = "available cannot be null.")
    Boolean available;
    @NotNull(message = "description cannot be null.")
    @NotEmpty(message = "available cannot be empty.")
    String description;
    @NotNull(message = "name cannot be null.")
    @NotEmpty(message = "name cannot be empty.")
    String name;
    @EqualsAndHashCode.Exclude
    List<Review> reviewList = new ArrayList<>();
}