package ru.practicum.shareit.booking.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class Review {
    Long id;
    Long clientId;
    Long reviewedItemId;
    Long bookingId;
    @NotNull
    Boolean positive;
    @NotNull
    String opinion;
    LocalDateTime reviewTime = LocalDateTime.now();
}
