package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestMapper;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ItemRequestMapperTest {
    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDtoResponse itemRequestDtoResponse;
    private ItemRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ItemRequestMapper();
        user = User.builder()
                .id(1L)
                .email("user1@email.ru")
                .name("User1")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("desc")
                .user(user)
                .created(LocalDateTime.now())
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .requestorId(1L)
                .description("desc")
                .build();

        itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(itemRequest.getId())
                .items(null)
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    @Test
    void mapTest() {

        assertThat(mapper.toRequest(itemRequestDto, user).equals(itemRequest));

        assertThat(mapper.toRequestDtoForResponse(itemRequest).equals(itemRequestDtoResponse));
    }
}