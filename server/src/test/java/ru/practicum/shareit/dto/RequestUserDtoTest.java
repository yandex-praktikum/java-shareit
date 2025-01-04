package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.practicum.shareit.user.dto.RequestUserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestUserDtoTest {
    private final ObjectMapper objectMapper;

    @Test
    void testSerialization() throws Exception {
        RequestUserDto requestUserDto = new RequestUserDto("John Doe", "john.doe@example.com");
        String jsonString = objectMapper.writeValueAsString(requestUserDto);
        assertThat(jsonString).contains("John Doe");
        assertThat(jsonString).contains("john.doe@example.com");
    }

    @Test
    void testDeserialization() throws Exception {
        String jsonString = "{\"name\":\"John Doe\", \"email\":\"john.doe@example.com\"}";
        RequestUserDto requestUserDto = objectMapper.readValue(jsonString, RequestUserDto.class);
        assertThat(requestUserDto.getName()).isEqualTo("John Doe");
        assertThat(requestUserDto.getEmail()).isEqualTo("john.doe@example.com");
    }
}
