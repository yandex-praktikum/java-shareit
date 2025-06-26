package shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
public class CommentDtoTest {
    private final LocalDateTime testTime = LocalDateTime.of(2025, 07, 25, 00, 00, 00);
    private final JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws IOException {
        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("authorName")
                .created(testTime)
                .build();

        JsonContent<CommentDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("authorName");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-07-25T00:00:00");
    }
}
