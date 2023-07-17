package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundRecordInBD;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@Slf4j
public class ValidationService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ValidationService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /**
     * Проверка пользователя на уникальность почты в БД при ОБНОВЛЕНИИ пользователя.
     * <p>Если почта принадлежит этому же пользователю, то всё хорошо.</p>
     * <p>Если почта принадлежит другому пользователю, то генерируется исключение.</p>
     *
     * @param user пользователь.
     * @throws ValidateException генерируемое исключение.
     */
    public void checkUniqueEmailToUpdate(User user) {
        final Long inputId = user.getId();
        final String inputEmail = user.getEmail();

        if (inputId == null) {
            //обновление не возможно, поскольку нет ID.
            String message = "1. Обновление пользователя невозможно, поскольку ID входящего пользователя " +
                    "должен быть не Null.";
            log.info(message + " Входящий пользователь: " + user);
            throw new NotFoundRecordInBD(message);
        }

        final Long idFromDbByEmail = userRepository.getUserIdByEmail(inputEmail);
        if (idFromDbByEmail != null && !inputId.equals(idFromDbByEmail)) {
            String message = String.format("2. Обновление пользователя невозможно, поскольку email = %s " +
                    "принадлежит пользователю с ID = %d.", inputEmail, idFromDbByEmail);
            log.info(message + " Входящий пользователь: " + user);
            throw new ConflictException(message);
        } //else if ()
    }


    /**
     * Проверка пользователя на уникальность почты.
     * <p>Если почта принадлежит этому же пользователю, то всё хорошо.</p>
     * <p>Если почта принадлежит другому пользователю, то генерируется исключение.</p>
     *
     * @param user пользователь.
     * @throws ConflictException генерируемое исключение.
     */
    public void checkUniqueEmailToCreate(User user) {
        final String inputEmail = user.getEmail();

        Long idFromDbByEmail = userRepository.getUserIdByEmail(inputEmail);
        //Надо проверить уникальность почты.
        if (idFromDbByEmail != null) {
            //В БД есть пользователь с данной почтой.
            String message = String.format("Создание пользователя невозможно, поскольку email = %s входящего " +
                    "принадлежит пользователю: %s.", inputEmail, userRepository.getUserById(idFromDbByEmail));
            log.info(message);
            throw new ConflictException(message);
        }
    }

    /**
     * Проверка всех полей пользователя.
     *
     * @param user пользователь.
     * @throws ValidateException генерируемое исключение.
     */
    public void validateUserFields(User user) {
        final String email = user.getEmail();
        if (email == null || email.isBlank()) {
            String error = "Email пользователя не может пустым.";
            log.info(error);
            throw new ValidateException(error);
        }

        final String name = user.getName();
        if (name == null || name.isBlank()) {
            String error = "Имя пользователя не может быть пустым.";
            log.info(error);
            throw new ValidateException(error);
        }
    }

    /**
     * Проверка полей пользователя.
     * <p>Если оба поля 'name' и 'email' равны null, то генерируется исключение.</p>
     *
     * @return массив boolean. Если поле null, то False.
     * <p>Первый элемент - имя, второй - email.</p>
     */
    public boolean[] checkFieldsForUpdate(User user) {
        boolean[] result;
        result = new boolean[2];
        final String name = user.getName();
        final String email = user.getEmail();
        result[0] = (name != null) && !name.isBlank();
        result[1] = (email != null) && !email.isBlank();

        if (result[0] || result[1]) {
            return result;
        }
        throw new ValidateException("Все поля пользователя равны 'null'.");
    }

    /**
     * Проверка наличия юзера в БД.
     *
     * @param userId пользователя.
     * @throws NotFoundRecordInBD - пользователь не найден в БД.
     */
    public User checkExistUserInDB(Long userId) {
        User result = userRepository.getUserById(userId);
        if (result == null) {
            String error = String.format("Error 404. Пользователь с ID = '%d' не найден в БД.", userId);
            log.info(error);
            throw new NotFoundRecordInBD(error);
        }
        return result;

    }

    /////////////////////////////////////////////////////////////////////////////////
    //////////                  Проверки для вещей.                   ///////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Проверка наличия вещи в БД.
     *
     * @param itemId ID вещи.
     * @return Null - вещь не найдена.
     * <p>Item -  вещь найдена.</p>
     * @throws NotFoundRecordInBD вещь не найдена в БД.
     */
    public Item checkExistItemInDB(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            String message = String.format("Вещь с ID = '%d' не найдена в БД.", itemId);
            log.info("Error 404. " + message);
            throw new NotFoundRecordInBD(message);
        }
        return item;
    }

    /**
     * Проверка отсутствия вещи в БД.
     *
     * @param itemId ID вещи.
     * @throws ConflictException Если вещь есть в БД, то генерируется исключение.
     */
    public void checkMissingItemInDB(Long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item != null) {
            String message = String.format("Вещь с ID = '%d' найдена в БД. %s", itemId, item);
            log.info("Error 409. " + message);
            throw new ConflictException(message);
        }
    }

    /**
     * Проверка всех полей вещей.
     *
     * @param item вещь.
     * @throws ValidateException генерируемое исключение.
     */
    public void validateItemFields(Item item) {
        final String name = item.getName();
        if (name == null || name.isBlank()) {
            String error = "Название вещи не может пустым.";
            log.info(error);
            throw new ValidateException(error);
        }

        final String description = item.getDescription();
        if (description == null || description.isBlank()) {
            String error = "Описание вещи не может быть пустым.";
            log.info(error);
            throw new ValidateException(error);
        }
        final Boolean available = item.getAvailable();
        if (available == null) {
            String error = "Для вещи необходим статус её бронирования.";
            log.info(error);
            throw new ValidateException(error);
        }
        final Long ownerId = item.getOwnerId();
        if (ownerId == null) {
            String error = "Для вещи необходим хозяин.";
            log.info(error);
            throw new ValidateException(error);
        }
    }


    /**
     * Проверка полей пользователя.
     * <p>Если оба поля 'name' и 'email' равны null, то генерируется исключение.</p>
     *
     * @return массив boolean. Если поле null, то False.
     * <p>Первый элемент - name, второй - description, третий - ownerId, четвёртый - available,</p>
     * <p>пятый - isRequest, шестой - отзывы.</p>
     */
    public boolean[] checkFieldsForUpdate(Item item) {
        boolean[] result;
        result = new boolean[3];
        result[0] = (item.getName() != null) && !item.getName().isBlank();
        result[1] = (item.getDescription() != null) && !item.getDescription().isBlank();
        result[2] = item.getAvailable() != null;

        for (boolean b : result) {
            if (b) return result;
        }
        throw new ValidateException("Все поля: название, описание и статус доступа к аренде равны 'null'.");
    }


    /**
     * Проверка: принадлежит ли вещь её хозяину.
     *
     * @return True - вещь принадлежит хозяину.
     * <p>False - вещь не принадлежит хозяину.</p>
     * @throws ValidateException Если вещь и (или) ID хозяина = null.
     */
    public boolean isOwnerItem(Item item, Long ownerId) {
        if (item == null || ownerId == null) {
            String message = "Вещь и (или) ID хозяина вещи равны null.";
            log.info("Error 400. {}", message);
            throw new ValidateException(message);
        }
        if (!ownerId.equals(item.getOwnerId())) {
            String message = String.format("Вещь %s не принадлежит хозяину с ID = %d.", item.getName(), ownerId);
            log.info("Error 404. {}", message);
            throw new NotFoundRecordInBD(message);
        }
        return (ownerId.equals(item.getOwnerId()));
    }
}
