package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.utilits.Variables;

@ExtendWith(MockitoExtension.class)
public class VariableUnitTest {

    @Test
    public void shouldNotModifyVariables() {
        Assertions.assertNotNull(Variables.USER_ID);
        Assertions.assertEquals(Variables.USER_ID, "X-Sharer-User-Id");
    }
}
