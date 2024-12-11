package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserSaveDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final UserService service;
    private UserDto userExpected;

    @BeforeEach
    void testInit() {
        userExpected = new UserDto();
        userExpected.setId(1);
        userExpected.setName("User1");
        userExpected.setEmail("user1@yandex.ru");
    }

    @Test
    void createUser() throws Exception {

        int userId = userExpected.getId();
        UserSaveDto userSaveDto = new UserSaveDto();
        userSaveDto.setName(userExpected.getName());
        userSaveDto.setEmail(userExpected.getEmail());
        String userSaveDtoJson = objectMapper.writeValueAsString(userSaveDto);

        when(service.createUser(any(UserSaveDto.class)))
                .thenReturn(userExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(userExpected);

        mockMvc.perform(post("/users")
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userSaveDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).createUser(any(UserSaveDto.class));
    }

    @Test
    void getUserTest() throws Exception {
        int userId = userExpected.getId();
        String path = "/users" + "/" + userId;

        when(service.getUser(eq(userId)))
                .thenReturn(userExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(userExpected);

        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).getUser(eq(userId));
    }

    @Test
    void updateUserTest() throws Exception {
        int userId = userExpected.getId();
        UserSaveDto userSaveDtoForUpdate = new UserSaveDto();
        String nameUpdated = "Superuser";
        userSaveDtoForUpdate.setName(nameUpdated);
        String emailUpdated = "superuser@yandex.ru";
        userSaveDtoForUpdate.setEmail(emailUpdated);
        String userSaveDtoForUpdateJson = objectMapper.writeValueAsString(userSaveDtoForUpdate);
        String path = "/users" + "/" + userId;

        when(service.updateUser(eq(userId), eq(userSaveDtoForUpdate)))
                .thenAnswer(invocationOnMock -> {
                    userExpected.setName(nameUpdated);
                    userExpected.setEmail(emailUpdated);
                    return userExpected;
                });
        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userSaveDtoForUpdateJson))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userExpected)));

        verify(service, times(1)).updateUser(eq(userId), eq(userSaveDtoForUpdate));
    }

    @Test
    void deleteUserTest() throws Exception {
        int userId = userExpected.getId();
        String path = "/users" + "/" + userId;
        mockMvc.perform(delete(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(eq(userId));
    }
}