package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.StorageData;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ItemDto extends StorageData {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long ownerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long requestId;
}

