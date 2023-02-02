package ru.practicum.shareit.Config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.user.UserService;


import static org.mockito.Mockito.mock;

@TestConfiguration
public class UserControllerTestConfig {
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }
}
