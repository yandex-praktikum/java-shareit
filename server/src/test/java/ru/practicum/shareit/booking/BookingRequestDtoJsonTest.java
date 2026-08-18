package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingRequestDtoJsonTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    @Test
    void shouldSerializeBookingRequest() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();

        dto.setItemId(100L);
        dto.setStart(LocalDateTime.of(2026, 8, 18, 16, 25, 20));
        dto.setEnd(LocalDateTime.of(2026, 8, 18, 17, 25, 20));

        String result = json.write(dto).getJson();

        assertThat(result).contains("\"itemId\":100");
        assertThat(result).contains("\"start\":\"2026-08-18T16:25:20\"");
        assertThat(result).contains("\"end\":\"2026-08-18T17:25:20\"");
    }

    @Test
    void shouldDeserializeBookingRequest() throws Exception {
        String content = """
                {
                    "itemId": 100,
                    "start": "2026-08-18T16:25:20",
                    "end": "2026-08-18T17:25:20"
                }
                """;

        BookingRequestDto dto = json.parseObject(content);

        assertThat(dto.getItemId()).isEqualTo(100L);
        assertThat(dto.getStart())
                .isEqualTo(LocalDateTime.of(2026, 8, 18, 16, 25, 20));
        assertThat(dto.getEnd())
                .isEqualTo(LocalDateTime.of(2026, 8, 18, 17, 25, 20));
    }
}