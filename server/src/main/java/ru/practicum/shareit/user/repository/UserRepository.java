package ru.practicum.shareit.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Integer> {
    Collection<User> findByEmail(String email);
}
