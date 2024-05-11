package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User user;
    private UserDto userDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        user = new User(1, "User", "user@mail.ru");
        userDto = new UserDto(1, "User", "user@mail.ru");
    }

    @Test
    void addUserTest() throws Exception {
        when(userService.addUser(any())).thenReturn(userDto);

        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).addUser(any());
    }

    @Test
    void patchUserTest() throws Exception {
        when(userService.updateUser(any(), eq(1))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .content(objectMapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).updateUser(any(), eq(1));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users/{userId}", user.getId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(eq(1));
    }

    @Test
    void getAllUsersTest() throws Exception {
        UserDto userDto1 = new UserDto(2, "User2", "user2@mail.ru");

        when(userService.getAllUsers()).thenReturn(List.of(userDto, userDto1));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userDto.getId())))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$[1].id", is(userDto1.getId())))
                .andExpect(jsonPath("$[1].name", is(userDto1.getName())))
                .andExpect(jsonPath("$[1].email", is(userDto1.getEmail())));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userService.getUserById(eq(1))).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).getUserById(eq(1));
    }
}

