package ru.practicum.shareit.Config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.item.ItemService;
import ru.practicum.item.ItemValidator;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ItemControllerTestConfig {
    @Bean
    public ItemService itemService() {
        return mock(ItemService.class);
    }

    @Bean
    public ItemValidator itemValidator() {
        return mock(ItemValidator.class);
    }
}
