package ru.practicum.shareit.booking.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    private static final String BOOKING_ID_PATH = "/{bookingId}";
    private static final String BOOKING_PATH = BOOKING_ID_PATH + "?approved={approved}";
    private static final String ALL_USER_BOOKINGS_PATH = "?state={state}&from={from}&size={size}";
    private static final String ALL_USER_ITEMS_BOOKINGS_PATH = "/owner/?state={state}";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addBooking(Integer userId, BookingSaveDto bookingSaveDto) {
        return post("", userId, bookingSaveDto);
    }

    public ResponseEntity<Object> manageBooking(Integer userId, Integer bookingId, Boolean approved) {
        Map<String, Object> uriVariables = Map.of("bookingId",bookingId,"approved", approved);
        return patch(BOOKING_PATH, userId, uriVariables);
    }

    public ResponseEntity<Object> getBooking(Integer userId, Integer bookingId) {
        Map<String, Object> uriVariables = Map.of("bookingId", bookingId);
        return get(BOOKING_ID_PATH, userId, uriVariables);
    }

    public ResponseEntity<Object> getAllUserBookings(Integer userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> uriVariables = Map.of("state", state.name(), "from", from, "size", size);
        return get(ALL_USER_BOOKINGS_PATH, userId, uriVariables);
    }

    public ResponseEntity<Object> getAllUserItemsBookings(Integer userId, BookingState state) {
        Map<String, Object> uriVariables = Map.of("state", state.name());
        return get(ALL_USER_ITEMS_BOOKINGS_PATH, userId, uriVariables);
    }
}
