package ru.practicum.shareit.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
   private String error;
   private String description;
}
