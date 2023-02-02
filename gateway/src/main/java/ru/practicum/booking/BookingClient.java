package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.dto.BookItemRequestDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(Long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from == null && size == null) {
            parameters = Map.of(
                    "state", state.name());
            return get("?state={state}", userId, parameters);
        } else {
            parameters = Map.of(
                    "state", state.name(),
                    "from", from,
                    "size", size);
            return get("?state={state}&from={from}&size={size}", userId, parameters);
        }
    }

    public ResponseEntity<Object> getBookingByOwner(Long ownerId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters;
        if (from == null && size == null) {
            parameters = Map.of(
                    "state", state.name());
            return get("/owner?state={state}", ownerId, parameters);
        } else {
            parameters = Map.of(
                    "state", state.name(),
                    "from", from,
                    "size", size);
            return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
        }
    }

    public ResponseEntity<Object> createBooking(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBookingById(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved);
        return patch("/" + bookingId + "?approved=" + approved, userId, parameters);
    }
}
