package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.UserEmailNotUniqueException;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createUserEndpointTest() throws Exception {
        Mockito
                .when(userService.create(any(User.class)))
                .thenAnswer(invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0, User.class);
                    user.setId(1);
                    return user;
                });

        User testUser = new User(0, "name", "e@mail.ru");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(testUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.email", is("e@mail.ru")));
    }

    @Test
    void createUserEndpointTestDuplicatedEmail() throws Exception {
        Mockito
                .when(userService.create(any(User.class)))
                .thenThrow(new UserEmailNotUniqueException("E-mail не уникален"));

        User testUser = new User(0, "name", "e@mail.ru");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(testUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void createUserEndpointTestValidation() throws Exception {
        User testUser = new User(0, "", "mail.ru");

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(testUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserEndpointTest() throws Exception {
        Mockito
                .when(userService.update(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, User.class));

        UserDto testUserDto = new UserDto(1, "name1", "e1@mail.ru");

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(testUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.email", is("e1@mail.ru")));
    }

    @Test
    void getAllUsersEndpointTest() throws Exception {
        User testUser = new User(0, "name1", "e1@mail.ru");

        Mockito
                .when(userService.getAll())
                .thenReturn(List.of(testUser));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testUser))));
    }

    @Test
    void getUserEndpointTest() throws Exception {
        User testUser = new User(0, "name1", "e1@mail.ru");

        Mockito
                .when(userService.getById(1))
                .thenReturn(testUser);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testUser)));
    }

    @Test
    void deleteUserEndpointTest() throws Exception {
        User testUser = new User(0, "name1", "e1@mail.ru");

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}