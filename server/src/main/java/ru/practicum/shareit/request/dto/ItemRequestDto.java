package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<ItemInfo> items;

    public ItemRequestDto(Long id, String description, Long requestorId, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
        this.items = Collections.emptyList();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        private Long id;
        private String name;
        private Long ownerId;
    }
}