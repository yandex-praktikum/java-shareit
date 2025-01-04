package ru.practicum.shareit.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import ru.practicum.shareit.item.dto.item.RequestItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestItemDtoTest {
    private final ObjectMapper objectMapper;

    @Test
    void testSerialization() throws Exception {
        RequestItemDto requestItemDto = new RequestItemDto("Item Name", "Description", true, 1L);
        String jsonString = objectMapper.writeValueAsString(requestItemDto);
        assertThat(jsonString).contains(
                requestItemDto.getName(), requestItemDto.getDescription(),
                String.valueOf(requestItemDto.getAvailable()),
                String.valueOf(requestItemDto.getRequestId())
        );
    }

    @Test
    void testDeserialization() throws Exception {
        String jsonString = "{\"name\":\"Item Name\",\"description\":\"Description\",\"available\":true,\"requestId\":1}";

        RequestItemDto requestItemDto = objectMapper.readValue(jsonString, RequestItemDto.class);

        assertThat(requestItemDto.getName()).isEqualTo("Item Name");
        assertThat(requestItemDto.getDescription()).isEqualTo("Description");
        assertThat(requestItemDto.getAvailable()).isTrue();
        assertThat(requestItemDto.getRequestId()).isEqualTo(1L);
    }
}