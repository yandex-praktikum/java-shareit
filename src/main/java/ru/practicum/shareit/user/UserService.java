package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    UserDto get(Long id); // Обязательно ли использовать обёртку Long? Или  можно примитив long? Если в модели используется обёртка Long
    Collection<UserDto> getAll();
    UserDto add(UserDto userDto);
    UserDto patch(UserDto userDto, Long id);
    Boolean delete(Long id);
}
