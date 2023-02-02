package ru.practicum.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(ItemRequestDto requestDto, Long userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> findRequestByRequestorId(Long requestorId) {
        return get("", requestorId);
    }

    public ResponseEntity<Object> findAllRequests(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from == null && size == null) {
            return get("/all", userId);
        } else {
            parameters = Map.of(
                    "from", from,
                    "size", size);
            return get("/all?from={from}&size={size}", userId, parameters);
        }
    }

    public ResponseEntity<Object> getRequestById(Long requestId, Long userId) {
        return get("/" + requestId, userId);
    }
}
