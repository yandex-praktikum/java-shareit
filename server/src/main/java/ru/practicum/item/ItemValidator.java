package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final UserRepository userRepo;

    public void userValidator(Long id) {
        User user = userRepo.getUserById(id);
        if (user == null) {
            throw new UserNotFoundException("can't find user");
        }
    }

    public void itemAddValidation(ItemDto item) {
        if (item.getAvailable() == null || item.getDescription() == null || item.getName() == null) {
            throw new ValidationException("nothing can't be null");
        } else if (item.getName().isBlank()) {
            throw new ValidationException("name can't be empty");
        }
    }
}
