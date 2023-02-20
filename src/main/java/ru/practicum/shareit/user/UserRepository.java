package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByEmail(String email);
}
