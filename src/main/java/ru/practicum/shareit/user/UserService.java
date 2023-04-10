package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    /**
     * Creates a new user
     * If the user has a duplicate e-mail address, throws BadUserException
     *
     * @param user
     * @return new user
     */
    User createUser(User user);

    /**
     * Returns user by id
     * If the user is not found throws NotFoundException
     *
     * @param id
     * @return user by id
     */
    User getUserById(Long id);

    /**
     * Returns a list of all users
     *
     * @return list of all users
     */
    List<User> getUsers();

    /**
     * Updates the user
     * If the user has a duplicate e-mail address, throws BadUserException
     *
     * @param user
     * @return updated user
     */
    User updateUser(User user);

    /**
     * Removes user by id
     * If the user is not found throws NotFoundException
     *
     * @param id
     */
    void removeUserById(Long id);
}
