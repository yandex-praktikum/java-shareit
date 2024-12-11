package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String ADD_COMMENT = "/{itemId}/comment";
    private static final String SEARCH_PATH = "/search?text=";
    private static final String ITEMS_PATH = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ITEMS_PATH))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addItem(Integer userId, ItemSaveDto itemSaveDto) {
        return post("", userId, itemSaveDto);
    }

    public ResponseEntity<Object> addComment(Integer userId, Integer itemId, CommentSaveDto commentSaveDto) {
        Map<String, Object> uriVariables = Map.of("itemId", itemId);
        return post(ADD_COMMENT, userId, uriVariables, commentSaveDto);
    }

    public ResponseEntity<Object> updateItem(Integer userId, Integer itemId, ItemSaveDto itemSaveDto) {
        String path = "/" + itemId.toString();
        return patch(path, userId, itemSaveDto);
    }

    public ResponseEntity<Object> getItem(Integer itemId) {
        String path = "/" + itemId.toString();
        return get(path);
    }

    public ResponseEntity<Object> getAllOwnerItems(Integer userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get(SEARCH_PATH + text);
    }
}
