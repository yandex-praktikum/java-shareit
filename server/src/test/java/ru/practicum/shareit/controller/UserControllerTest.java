package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserEmailIsNotUnique;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "User name", "user@gmail.com");
    }

    @Test
    void findAll_shouldReturnUsers() throws Exception {
        when(userService.getAll()).thenReturn(Collections.singleton(userDto));
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()));
    }

    @Test
    void findById_shouldReturnUser() throws Exception {
        when(userService.findUserById(eq(userDto.getId()))).thenReturn(userDto);

        mockMvc.perform(get("/users/" + userDto.getId()))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void findById_shouldThrowNotFoundException_whenUserDoesNotExists() throws Exception {
        Long userId = userDto.getId();

        String exMessage = format("User по id=%d не найден", userId);
        when(userService.findUserById(eq(userId))).thenThrow(
                new NotFoundException(exMessage));

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Запрашиваемый ресурс не найден"))
                .andExpect(jsonPath("$.description").value(exMessage));
    }

    @Test
    void createUser_userShouldBeCreated() throws Exception {
        RequestUserDto requestUserDto = new RequestUserDto("User name", "user@gmail.com");
        when(userService.create(any(RequestUserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void createUser_shouldThrowUserEmailIsNotUnique_whenEmailIsNotValid() throws Exception {
        RequestUserDto requestUserDto = new RequestUserDto("User name", "user@gmail.com");
        String exMessage = "Пользователь с таким email уже существует";

        when(userService.create(any(RequestUserDto.class)))
                .thenThrow(new UserEmailIsNotUnique(exMessage));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(jsonPath("$.error").value("Запрос не может быть выполнен"))
                .andExpect(jsonPath("$.description").value(exMessage));
    }

    @Test
    void updateUser_userShouldBeCreated() throws Exception {
        RequestUserDto requestUserDto = new RequestUserDto("User name", "user@gmail.com");
        long userId = 1L;

        when(userService.update(eq(userId), any(RequestUserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch(format("/users/%d", userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void updateUser_shouldThrowUserEmailIsNotUnique_whenEmailIsNotValid() throws Exception {
        RequestUserDto requestUserDto = new RequestUserDto("User name", "user@gmail.com");
        long userId = 1L;
        String exMessage = "Пользователь с таким email уже существует";

        when(userService.update(eq(userId), any(RequestUserDto.class)))
                .thenThrow(new UserEmailIsNotUnique(exMessage));

        mockMvc.perform(patch(format("/users/%d", userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUserDto)))
                .andExpect(jsonPath("$.error").value("Запрос не может быть выполнен"))
                .andExpect(jsonPath("$.description").value(exMessage));
    }

    @Test
    void deleteUser_userShouldBeDeleted() throws Exception {
        long userId = 1L;

        mockMvc.perform(delete(format("/users/%d", userId)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_shouldReturnNotFoundException() throws Exception {
        Long userId = 1L;
        String exMessage = format("User по id=%d не найден", userId);

        doThrow(new NotFoundException(exMessage)).when(userService).deleteById(userId);

        mockMvc.perform(delete(format("/users/%d", userId)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Запрашиваемый ресурс не найден"))
                .andExpect(jsonPath("$.description").value(exMessage));
    }
}
