package ru.practicum.shareit.Config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.request.ItemRequestService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ItemRequestControllerTestConfig {
    @Bean
    public ItemRequestService requestService() {
        return mock(ItemRequestService.class);
    }
}
