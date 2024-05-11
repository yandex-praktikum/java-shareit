package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItem(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsPagination(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> updateItem(long userId, ItemDto item, long itemId) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> addItem(long userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> delete(long userId, long itemId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> search(long userId, int from, int size, String text) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "text", text
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}
