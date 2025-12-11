package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeUserDto() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@mail.com");
    }

    @Test
    void deserializeUserDto() throws Exception {
        String content = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john@mail.com\"}";

        UserDto result = objectMapper.readValue(content, UserDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@mail.com");
    }

    @Test
    void serializeUserDtoWithEmptyName() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("");
        userDto.setEmail("john@mail.com");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@mail.com");
    }

    @Test
    void serializeUserDtoWithInvalidEmail() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        userDto.setEmail("invalid-email");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("invalid-email");
    }

    @Test
    void deserializeUserDtoWithNullName() throws Exception {
        String content = "{\"id\": 1, \"email\": \"john@mail.com\"}";

        UserDto result = objectMapper.readValue(content, UserDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isNull();
        assertThat(result.getEmail()).isEqualTo("john@mail.com");
    }

    @Test
    void deserializeUserDtoWithNullEmail() throws Exception {
        String content = "{\"id\": 1, \"name\": \"John Doe\"}";

        UserDto result = objectMapper.readValue(content, UserDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isNull();
    }

    @Test
    void deserializeUserDtoWithEmptyEmail() throws Exception {
        String content = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"\"}";

        UserDto result = objectMapper.readValue(content, UserDto.class);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("");
    }
}
