package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * Добавить юзера в БД.
     *
     * @param user пользователь.
     * @return добавляемый пользователь.
     */
    User addToStorage(User user);

    /**
     * Обновить юзера в БД.
     *
     * @param user пользователь
     * @return обновлённый пользователь.
     */
    User updateInStorage(User user, boolean[] isUpdateField);

    /**
     * Удалить пользователя из БД.
     *
     * @param id ID удаляемого пользователя.
     */
    void removeFromStorage(Long id);

    /**
     * Получить список всех пользователей.
     *
     * @return список пользователей.
     */
    List<User> getAllUsersFromStorage();

    /**
     * Получить пользователя по ID.
     *
     * @param id ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    User getUserById(Long id);


    /**
     * Проверка наличия юзера в БД.
     *
     * @param id пользователя.
     * @return True - пользователь найден. False - пользователя нет в БД.
     */
    boolean isExistUserInDB(Long id);

    /**
     * Проверка наличия пользователя по `Email`.
     *
     * @param email адрес эл. почты нового пользователя.
     * @return ID пользователя с Email из БД. Null - нет такого email в БД.
     */
    Long getUserIdByEmail(String email);
}
