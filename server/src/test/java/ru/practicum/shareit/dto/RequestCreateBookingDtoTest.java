package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.practicum.shareit.booking.dto.RequestCreateBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestCreateBookingDtoTest {
    private final ObjectMapper objectMapper;

    @Test
    void testSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 10, 0);
        RequestCreateBookingDto requestCreateBookingDto = new RequestCreateBookingDto(1L, start, end);

        String jsonString = objectMapper.writeValueAsString(requestCreateBookingDto);

        assertThat(jsonString).contains("1", start.toString(), end.toString());
    }

    @Test
    void testDeserialization() throws Exception {
        String jsonString = "{\"itemId\":1,\"start\":\"2023-10-01T10:00:00\",\"end\":\"2023-10-02T10:00:00\"}";

        RequestCreateBookingDto requestCreateBookingDto = objectMapper.readValue(jsonString, RequestCreateBookingDto.class);

        assertThat(requestCreateBookingDto.getItemId()).isEqualTo(1L);
        assertThat(requestCreateBookingDto.getStart()).isEqualTo(LocalDateTime.of(2023, 10, 1, 10, 0));
        assertThat(requestCreateBookingDto.getEnd()).isEqualTo(LocalDateTime.of(2023, 10, 2, 10, 0));
    }
}
