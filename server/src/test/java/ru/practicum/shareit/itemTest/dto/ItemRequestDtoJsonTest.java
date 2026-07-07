package ru.practicum.shareit.itemTest.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setName("Дрель");
        dto.setDescription("Аккумуляторная");
        dto.setAvailable(true);
        dto.setRequestId(5L);

        String result = json.write(dto).getJson();

        assertThat(result).contains("\"name\":\"Дрель\"");
        assertThat(result).contains("\"description\":\"Аккумуляторная\"");
        assertThat(result).contains("\"available\":true");
        assertThat(result).contains("\"requestId\":5");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Молоток\",\"description\":\"Тяжелый\",\"available\":false}";

        ItemRequestDto dto = json.parseObject(content);

        assertThat(dto.getName()).isEqualTo("Молоток");
        assertThat(dto.getDescription()).isEqualTo("Тяжелый");
        assertThat(dto.getAvailable()).isFalse();
    }
}