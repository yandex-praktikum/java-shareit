package ru.practicum.shareit.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration // помечает класс как java-config для контекста приложения
@EnableWebMvc  // призывает импортировать дополнительную конфигурацию для веб-приложений
public class WebConfig {
}
