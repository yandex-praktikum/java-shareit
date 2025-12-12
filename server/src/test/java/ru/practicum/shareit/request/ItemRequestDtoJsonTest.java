package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void serializeItemRequestDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "Нужна дрель для ремонта",
                2L,
                created,
                List.of(
                        new ItemRequestDto.ItemInfo(10L, "Дрель", 3L)
                )
        );

        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужна дрель для ремонта");
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].ownerId").isEqualTo(3);
    }

    @Test
    void deserializeItemRequestDto() throws Exception {
        String content = "{\"id\": 1, \"description\": \"Нужна дрель\", \"requestorId\": 2, " +
                "\"created\": \"2024-01-01T12:00:00\", \"items\": [{\"id\": 10, \"name\": \"Дрель\", \"ownerId\": 3}]}";

        ItemRequestDto result = objectMapper.readValue(content, ItemRequestDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Нужна дрель");
        assertThat(result.getRequestorId()).isEqualTo(2L);
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getId()).isEqualTo(10L);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Дрель");
        assertThat(result.getItems().get(0).getOwnerId()).isEqualTo(3L);
    }

    @Test
    void serializeItemRequestDtoWithoutItems() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "Нужна дрель для ремонта",
                2L,
                created
        );

        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужна дрель для ремонта");
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }

    @Test
    void deserializeItemRequestDtoWithoutItems() throws Exception {
        String content = "{\"id\": 1, \"description\": \"Нужна дрель\", \"requestorId\": 2, " +
                "\"created\": \"2024-01-01T12:00:00\"}";

        ItemRequestDto result = objectMapper.readValue(content, ItemRequestDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Нужна дрель");
        assertThat(result.getRequestorId()).isEqualTo(2L);
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
        assertThat(result.getItems()).isNull(); // Изменено: без items в JSON поле будет null
    }

    @Test
    void serializeItemRequestDtoWithEmptyDescription() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                "",
                2L,
                created
        );

        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("");
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-01-01T12:00:00");
    }

    @Test
    void serializeItemRequestDtoWithNullDescription() throws Exception {
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        ItemRequestDto requestDto = new ItemRequestDto(
                1L,
                null,
                2L,
                created
        );

        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isNullOrEmpty();
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-01-01T12:00:00");
    }

    @Test
    void deserializeItemRequestDtoWithEmptyDescription() throws Exception {
        String content = "{\"id\": 1, \"description\": \"\", \"requestorId\": 2, " +
                "\"created\": \"2024-01-01T12:00:00\"}";

        ItemRequestDto result = objectMapper.readValue(content, ItemRequestDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("");
        assertThat(result.getRequestorId()).isEqualTo(2L);
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
    }

    @Test
    void deserializeItemRequestDtoWithoutRequestorId() throws Exception {
        String content = "{\"id\": 1, \"description\": \"Нужна дрель\", " +
                "\"created\": \"2024-01-01T12:00:00\"}";

        ItemRequestDto result = objectMapper.readValue(content, ItemRequestDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Нужна дрель");
        assertThat(result.getRequestorId()).isNull();  // Без @NotNull может быть null
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
    }
}