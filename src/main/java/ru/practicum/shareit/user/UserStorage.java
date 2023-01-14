package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserStorage {
    User get(Long id); // Обязательно ли использовать обёртку Long? Или  можно примитив long? Если в модели используется обёртка Long
    Collection<User> getAll();
    User add(User user);
    User patch (User user);
    Boolean delete(Long id);
}
