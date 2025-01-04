package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.RequestUserDto;

import static java.lang.String.format;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(format("%s%s", serverUrl, API_PREFIX)))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getUsers() {
        return get("");
    }

    public ResponseEntity<Object> getUser(Long userId) {
        return get(format("/%d", userId));
    }

    public ResponseEntity<Object> createUser(RequestUserDto userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> patchUser(Long userId, RequestUserDto userDto) {
        return patch(format("/%d", userId), userDto);
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        return delete(format("/%d", userId));
    }
}
