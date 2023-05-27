package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Date;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceIntegTest {
    private final ItemRequestService requestService;
    private final UserService userService;

    @BeforeEach
    public void setUp() {
        createRequestor();
    }

    @Test
    public void shouldSuccessAddRequest() {
        UserDto requestorDto = userService.getUser(1);
        ItemRequestDto itemRequest = new ItemRequestDto(null, "Крньки 39 размера", requestorDto, new Date(), null);
        ItemRequestDto newItemRequest = requestService.addItemRequest(1, itemRequest);

        Assertions.assertNotNull(newItemRequest);
        Assertions.assertEquals(newItemRequest.getDescription(), itemRequest.getDescription());
    }

    private void createRequestor() {
        UserDto userDto = new UserDto();
        userDto.setEmail("ttt@email.ru");
        userDto.setName("Tanya");
        userService.addUser(userDto);
    }
}
