package ru.practicum.shareit.userTest.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Иван");
        dto.setEmail("ivan@mail.com");

        JsonContent<UserDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Иван");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ivan@mail.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"name\":\"Петр\",\"email\":\"petr@mail.com\"}";

        UserDto dto = json.parseObject(content);

        assertThat(dto.getName()).isEqualTo("Петр");
        assertThat(dto.getEmail()).isEqualTo("petr@mail.com");
    }
}