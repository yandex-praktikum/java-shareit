package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemValidator;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemValidatorTest {
    private ItemValidator validator;
    private Item item;
    private ItemDto itemDto;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        validator = new ItemValidator(userRepository);

        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(1L)
                .build();
    }

    @Test
    public void validationTest() {
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> validator.userValidator(1L));

        itemDto.setName("");

        assertThrows(ValidationException.class, () -> validator.itemAddValidation(itemDto));

        itemDto.setName("asd");
        itemDto.setDescription(null);

        assertThrows(ValidationException.class, () -> validator.itemAddValidation(itemDto));
    }
}