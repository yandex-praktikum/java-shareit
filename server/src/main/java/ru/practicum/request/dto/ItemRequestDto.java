package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Jacksonized
public class ItemRequestDto {

    Long requestorId;

    @NotNull
    @NotBlank
    String description;
}
