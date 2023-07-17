package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundRecordInBD;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.ValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final UserMapper mapper;

    public UserServiceImpl(@Qualifier("InMemory") UserRepository userRepository,
                           ValidationService validationService, UserMapper mapper) {
        this.userRepository = userRepository;
        this.validationService = validationService;
        this.mapper = mapper;
    }

    /**
     * Получить пользователя по ID.
     * @param id ID пользователя.
     * @return UserDto - пользователь присутствует в библиотеке.
     * <p>null - пользователя нет в библиотеке.</p>
     */
    @Override
    public UserDto getUserById(Long id) {
        User result = userRepository.getUserById(id);
        if (result == null) {
            String error = "В БД отсутствует запись о пользователе при получении пользователя по ID = " + id + ".";
            log.info(error);
            throw new NotFoundRecordInBD(error);
        }
        String message = String.format("Выдан ответ на запрос пользователя по ID = %d:\t%s", id, result);
        log.info(message);

        return mapper.mapToDto(result);
    }

    /**
     * Получение списка всех пользователей.
     * @return Список пользователей.
     */
    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> allUsersDto = new ArrayList<>();
        List<User> allUsers = userRepository.getAllUsersFromStorage();
        allUsers.stream().map(mapper::mapToDto).forEach(allUsersDto::add);
        log.info("Выдан список всех пользователей.");
        return allUsersDto;
    }

    /**
     * Добавить юзера в БД.
     * @param userDto пользователь.
     * @return добавляемый пользователь.
     */
    @Override
    public UserDto addToStorage(UserDto userDto) throws ValidateException, NotFoundRecordInBD {
        User user = mapper.mapToModel(userDto);

        validationService.validateUserFields(user);
        validationService.checkUniqueEmailToCreate(user);
        UserDto createdUser = mapper.mapToDto(userRepository.addToStorage(user));
        String message = String.format("В БД добавлен новый пользователь:\t%s", createdUser);
        log.info(message);
        return createdUser;
    }

    /**
     * Обновить юзера в БД.
     * @param userDto пользователь
     * @param userId  ID обновляемого юзера.
     * @return обновлённый пользователь.
     */
    @Override
    public UserDto updateInStorage(UserDto userDto, Long userId) {

        userDto.setId(userId);
        User user = mapper.mapToModel(userDto);
        validationService.checkExistUserInDB(user.getId());
        boolean[] isUpdateFields = validationService.checkFieldsForUpdate(user);
        validationService.checkUniqueEmailToUpdate(user);

        User updatedUser = userRepository.updateInStorage(user, isUpdateFields);
        log.info("Выполнено обновление пользователя в БД.");
        return mapper.mapToDto(updatedUser);
    }

    /**
     * Удалить пользователя из БД.
     * @param id ID удаляемого пользователя
     * @throws NotFoundRecordInBD из метода validationService.checkExistUserInDB(id).
     */
    @Override
    public String removeFromStorage(Long id) {
        User deletedUser = validationService.checkExistUserInDB(id);
        userRepository.removeFromStorage(id);
        String message = String.format("Выполнено удаление пользователя с ID = %d. %s", id, deletedUser);
        log.info(message);
        return message;
    }

    /**
     * Проверка наличия пользователя по `Email`.
     * @param newEmail адрес эл. почты нового пользователя.
     * @return ID пользователя с Email, если он есть в БД.
     * <p>Null, если нет.</p>
     */
    @Override
    public Long getUserIdByEmail(String newEmail) {
        return userRepository.getUserIdByEmail(newEmail);
    }


}
