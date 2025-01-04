package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestItemRequestDtoTest {
    private final ObjectMapper objectMapper;

    @Test
    void testSerialization() throws Exception {
        RequestItemRequestDto requestItemRequestDto = new RequestItemRequestDto("Sample description");
        String jsonString = objectMapper.writeValueAsString(requestItemRequestDto);
        assertThat(jsonString).contains("Sample description");
    }

    @Test
    void testDeserialization() throws Exception {
        String jsonString = "{\"description\":\"Sample description\"}";
        RequestItemRequestDto requestItemRequestDto = objectMapper.readValue(jsonString, RequestItemRequestDto.class);
        assertThat(requestItemRequestDto.getDescription()).isEqualTo("Sample description");
    }
}
