package ru.practicum.shareit.storage.request;

import org.apache.coyote.Request;

import java.util.HashMap;

public class InMemoryRequestStorage implements RequestStorage {
    private HashMap<Integer, Request> requestHashMap = new HashMap<>();
    private Integer generatedRequestId;
}
