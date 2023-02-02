package ru.practicum.shareit.Config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.booking.BookingService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class BookingControllerTestConfig {
    @Bean
    public BookingService bookingService() {
        return mock(BookingService.class);
    }
}
