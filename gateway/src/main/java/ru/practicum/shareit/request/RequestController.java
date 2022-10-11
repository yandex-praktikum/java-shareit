package java.ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    public RequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody RequestDto itemRequestDto) {
        log.info("create request");
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getByRequestor(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("get requests by user {}", userId);
        return requestClient.getByRequestor(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestParam(name = "from", defaultValue = "1") int from,
                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("get requests all");
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long requestId) {
        log.info("get request id = {}", requestId);
        return requestClient.getById(userId, requestId);
    }
}
