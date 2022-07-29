package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Серилизатор для поля available объекта ItemDto
 */
@Component
public class StringBooleanSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (s.equals("true")) {
            jsonGenerator.writeBoolean(true);
        } else {
            jsonGenerator.writeBoolean(false);
        }
    }
}