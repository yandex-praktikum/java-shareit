package ru.practicum.shareit.userTest.controller;

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
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.mapper.UserMapperMapstruct;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapperMapstruct userMapper;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDto userDto;
    private UserDto userDto1;
    private UserDto userDto2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userDto = new UserDto();
        userDto.setEmail("john.1@mail.com");
        userDto.setName("John 1");

        userDto1 = new UserDto();
        userDto1.setEmail("john.doe@mail.com");
        userDto1.setName("John Doe");

        userDto2 = new UserDto();
        userDto2.setEmail("jane.smith@mail.com");
        userDto2.setName("Jane Smith");

        user1 = new User();
        user1.setId(1L);
        user1.setEmail("john.doe@mail.com");
        user1.setName("John Doe");

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("jane.smith@mail.com");
        user2.setName("Jane Smith");
    }

    @Test
    void saveNewUser() throws Exception {
        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("john.doe@mail.com");
        newUser.setName("John Doe");

        when(userMapper.mapToUser(any(UserDto.class))).thenReturn(newUser);
        when(userService.createUser(any(User.class))).thenReturn(newUser);
        when(userMapper.mapToDto(newUser)).thenReturn(userDto1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));
        when(userMapper.mapToDto(user1)).thenReturn(userDto1);
        when(userMapper.mapToDto(user2)).thenReturn(userDto2);

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].email", is("john.doe@mail.com")))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].email", is("jane.smith@mail.com")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }
}