package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.RequestItem;

import java.util.List;

public interface RequestItemService {

    RequestItem createRequestItem(RequestItem requestItem, Long requestorId);

    List<RequestItem> getAllRequestItemByOwner(Long ownerId);

    List<RequestItem> getAllRequestItemAllUsers(Long currentUserId);

    RequestItem getRequestItemById(Long requestId);
}
