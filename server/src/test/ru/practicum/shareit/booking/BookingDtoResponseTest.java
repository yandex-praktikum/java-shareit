package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.booking.Status;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class BookingDtoResponseTest {
    @Autowired
    private JacksonTester<BookingDtoResponse> json;

    @Test
    void testBookingDtoSerialization() throws IOException {
        LocalDateTime start = LocalDateTime.of(2023, 1, 10,
                12, 30, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 2, 10,
                12, 30, 0, 0);
        User user = new User(5L, "User1", "user@email.com");
        Item item = new Item(7L, "Item", "best item", true, 5L, null);

        var bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .status(Status.APPROVED)
                .start(start)
                .end(end)
                .booker(user)
                .item(item)
                .build();

        var expected = "{\"id\":1,\"start\":\"2023-01-10T12:30:00\",\"end\":\"2023-02-10T12:30:00\",\"status\":\"" +
                "APPROVED\",\"booker\":{\"id\":5,\"name\":\"User1\",\"email\":\"user@email.com\"},\"item\":{\"id\":7," +
                "\"name\":\"Item\",\"description\":\"best item\",\"available\":true,\"ownerId\":5}}";

        assertThat((json.write(bookingDtoResponse).equals(expected)));
    }

    @Test
    void testBookingDtoDeserialization() throws IOException {
        LocalDateTime start = LocalDateTime.of(2023, 1, 10,
                12, 30, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 2, 10,
                12, 30, 0, 0);
        User user = new User(5L, "User1", "user@email.com");
        Item item = new Item(7L, "Item", "best item", true, 5L, null);
        var expected = BookingDtoResponse.builder()
                .id(1L)
                .status(Status.APPROVED)
                .start(start)
                .end(end)
                .booker(user)
                .item(item)
                .build();

        var jsonValue = "{\"id\":1,\"start\":\"2023-01-10T12:30:00\",\"end\":\"2023-02-10T12:30:00\",\"status\":\"" +
                "APPROVED\",\"booker\":{\"id\":5,\"name\":\"User1\",\"email\":\"user@email.com\"},\"item\":{\"id\":7," +
                "\"name\":\"Item\",\"description\":\"best item\",\"available\":true,\"ownerId\":5}}";
        assertThat(json.parse(jsonValue).equals(expected));
    }
}
