package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void serialize_shouldWriteLocalDateTime() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("Нужна дрель");
        dto.setCreated(LocalDateTime.of(2026, 8, 18, 15, 30));
        dto.setItems(List.of());

        String jsonContent = json.write(dto).getJson();

        assertThat(jsonContent).contains("\"id\":1");
        assertThat(jsonContent).contains("\"description\":\"Нужна дрель\"");
        assertThat(jsonContent)
                .contains("\"created\":\"2026-08-18T15:30:00\"");
        assertThat(jsonContent).contains("\"items\":[]");
    }

    @Test
    void deserialize_shouldReadLocalDateTime() throws Exception {
        String jsonContent = """
                {
                  "id": 1,
                  "description": "Нужна дрель",
                  "created": "2026-08-18T15:30:00",
                  "items": []
                }
                """;

        ItemRequestDto dto = json.parseObject(jsonContent);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Нужна дрель");
        assertThat(dto.getCreated())
                .isEqualTo(LocalDateTime.of(2026, 8, 18, 15, 30));
        assertThat(dto.getItems()).isEmpty();
    }
}