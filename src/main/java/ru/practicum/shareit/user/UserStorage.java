package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    /**
     * Creates a new user
     *
     * @param user
     * @return new user
     */
    User createUser(User user);

    /**
     * Returns a user by id
     *
     * @param id
     * @return user or null if there was no one
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
     *
     * @param user
     * @return updated user
     */
    User updateUser(User user);

    /**
     * Removes a user by id
     *
     * @param id
     */
    void removeUserById(Long id);
}
