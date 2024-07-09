package ru.practicum.shareit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.StorageData;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static long getNextId(final Collection<? extends StorageData> data) {
        return data.stream()
                .map(StorageData::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
