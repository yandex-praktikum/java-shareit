package ru.practicum.shareit.review;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "create")
public class Review {
    long id;
    long userId;
    long itemId;
    String reviewText;
}
