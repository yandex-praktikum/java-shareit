package ru.practicum.shareit.user.service;


import ru.practicum.shareit.exception.NotFoundRecordInBD;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    /**
     * Получить пользователя по ID.
     * @param id ID пользователя.
     * @return User - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    UserDto getUserById(Long id);

    /**
     * Получение списка всех пользователей.
     * @return Список пользователей.
     */
    List<UserDto> getAllUsers();


    /**
     * Добавить юзера в БД.
     * @param userDto пользователь.
     * @return добавляемый пользователь.
     */
    UserDto addToStorage(UserDto userDto) throws ValidateException, NotFoundRecordInBD;

    /**
     * Обновить юзера в БД.
     * @param userDto пользователь
     * @param userId  ID обновляемого юзера.
     * @return обновлённый пользователь.
     */
    UserDto updateInStorage(UserDto userDto, Long userId);

    /**
     * Удалить пользователя из БД.
     * @param id ID удаляемого пользователя
     * @throws NotFoundRecordInBD из метода validationService.checkExistUserInDB(id).
     */
    String removeFromStorage(Long id);

    /**
     * Проверка наличия пользователя по `Email`.
     * @param email адрес эл. почты нового пользователя.
     * @return True - пользователь с Email есть в БД. False - нет.
     */
    Long getUserIdByEmail(String email);

}
