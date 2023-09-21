package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    Long id;
    Long reviewedItemId;
    Boolean positive;
    String opinion;
    LocalDateTime reviewTime;
}
