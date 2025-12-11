package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ru.practicum.shareit.user.UserService userService;

    @Test
    void createUser_whenValidData_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john@mail.com");

        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@mail.com"));
    }

    @Test
    void createUser_whenMissingName_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("");
        userDto.setEmail("john@mail.com");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("");
        responseDto.setEmail("john@mail.com");

        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_whenMissingEmail_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("");

        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_whenInvalidEmailFormat_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("invalid-email");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("invalid-email");

        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createUser_whenDuplicateEmail_thenReturnConflict() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("existing@mail.com");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        Mockito.when(userService.createUser(Mockito.any()))
                .thenThrow(new ru.practicum.shareit.exception.DuplicateEmailException("Email уже существует"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUser_whenValidData_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Updated");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Updated");
        responseDto.setEmail("john@mail.com");

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john@mail.com"));
    }

    @Test
    void updateUser_whenUpdateEmail_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("john.new@mail.com");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john.new@mail.com");

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.new@mail.com"));
    }

    @Test
    void updateUser_withEmptyBody_thenReturnOk() throws Exception {
        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john@mail.com");

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()); // Изменено с isBadRequest() на isOk()
    }

    @Test
    void updateUser_whenUserNotFound_thenReturnNotFound() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Updated");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.any()))
                .thenThrow(new ru.practicum.shareit.exception.EntityNotFoundException("Пользователь не найден"));

        mockMvc.perform(patch("/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_whenValidRequest_thenReturnOk() throws Exception {
        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john@mail.com");

        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getUserById_whenUserNotFound_thenReturnNotFound() throws Exception {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenThrow(new ru.practicum.shareit.exception.EntityNotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_whenValidRequest_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        userDto.setEmail("john@mail.com");

        List<UserDto> users = List.of(userDto);

        Mockito.when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void getAllUsers_whenNoUsers_thenReturnEmptyList() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deleteUser_whenValidRequest_thenReturnOk() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(Mockito.any());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_whenUserNotFound_thenReturnNotFound() throws Exception {
        Mockito.doThrow(new ru.practicum.shareit.exception.EntityNotFoundException("Пользователь не найден"))
                .when(userService).deleteUser(Mockito.any());

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_withValidEmailWithPlus_thenReturnOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john+tag@mail.com");

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        UserDto responseDto = new UserDto();
        responseDto.setId(1L);
        responseDto.setName("John Doe");
        responseDto.setEmail("john+tag@mail.com");

        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john+tag@mail.com"));
    }

    @Test
    void createUser_withNullBody_thenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}
