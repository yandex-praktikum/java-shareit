package ru.practicum.shareit.bookingTest.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.model.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingRequestDtoJsonTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.of(2025, 12, 20, 10, 0));
        dto.setEnd(LocalDateTime.of(2025, 12, 25, 18, 0));

        String result = json.write(dto).getJson();

        assertThat(result).contains("\"itemId\":1");
        assertThat(result).contains("\"start\":\"2025-12-20T10:00:00\"");
        assertThat(result).contains("\"end\":\"2025-12-25T18:00:00\"");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"itemId\":1,\"start\":\"2025-12-20T10:00:00\",\"end\":\"2025-12-25T18:00:00\"}";

        BookingRequestDto dto = json.parseObject(content);

        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2025, 12, 20, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2025, 12, 25, 18, 0));
    }
}