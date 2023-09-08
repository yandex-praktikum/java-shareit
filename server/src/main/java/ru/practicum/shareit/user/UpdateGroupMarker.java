package ru.practicum.shareit.user;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, UpdateGroupMarker.UpdateMarker.class})
public interface UpdateGroupMarker {
    interface UpdateMarker {
    }
}

