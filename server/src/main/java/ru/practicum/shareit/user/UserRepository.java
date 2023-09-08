package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query(value = "UPDATE USERS it SET EMAIL = coalesce(:email, EMAIL),\n" +
            "    NAME = coalesce(:name, NAME)\n" +
            "    WHERE ID =:id", nativeQuery = true)
    Integer update(@Param("id") Long id,
                   @Param("email") String email,
                   @Param("name") String name);
}
