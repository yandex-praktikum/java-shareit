package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserSaveDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final EntityManager em;
    private final UserService service;
    private final UserMapper mapper;
    private UserDto userCreatedExpected;
    private UserDto userExistedExpected;

    private static final long NON_EXISTENT_ID = 999;

    @BeforeEach
    public void testInit() {
        userCreatedExpected = new UserDto();
        userCreatedExpected.setId(1);
        userCreatedExpected.setName("user");
        userCreatedExpected.setEmail("user@somemail.ru");
        userExistedExpected = new UserDto();
        userExistedExpected.setId(10);
        userExistedExpected.setName("user1");
        userExistedExpected.setEmail("user1@somemail.ru");
    }

    @Test
    void createUserTest() {
        UserSaveDto userSaveDto = new UserSaveDto();
        userSaveDto.setName(userCreatedExpected.getName());
        userSaveDto.setEmail(userCreatedExpected.getEmail());
        int userCreatedExpectedId = userCreatedExpected.getId();

        service.createUser(userSaveDto);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u where u.id = :userId", User.class);
        UserDto userCreated = mapper.map(query.setParameter("userId", userCreatedExpectedId).getSingleResult());

        assertThat(userCreated, allOf(
                hasProperty("id", equalTo(userCreatedExpectedId)),
                hasProperty("name", equalTo(userCreatedExpected.getName())),
                hasProperty("email", equalTo(userCreatedExpected.getEmail()))
        ));
    }

    @Test
    void getUserTest() {
        int userId = userExistedExpected.getId();

        UserDto user = service.getUser(userId);

        assertThat(user, allOf(
                hasProperty("id", equalTo(userExistedExpected.getId())),
                hasProperty("name", equalTo(userExistedExpected.getName())),
                hasProperty("email", equalTo(userExistedExpected.getEmail()))
        ));
    }

    @Test
    void updateUserTest() {
        int userId = 10;
        UserSaveDto userSaveDtoForUpdate = new UserSaveDto();
        userSaveDtoForUpdate.setName("user");
        userSaveDtoForUpdate.setEmail("user@yandex.ru");

        service.updateUser(userId, userSaveDtoForUpdate);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u where u.id = :userId", User.class);
        UserDto userUpdated = mapper.map(query.setParameter("userId", userId).getSingleResult());

        assertThat(userUpdated, allOf(
                hasProperty("id", equalTo(userId)),
                hasProperty("name", equalTo(userSaveDtoForUpdate.getName())),
                hasProperty("email", equalTo(userSaveDtoForUpdate.getEmail()))
        ));
    }

    @Test
    void deleteUserTest() {
        int userId = 60;
        service.deleteUser(userId);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u", User.class);

        assertTrue(query.getResultStream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .isEmpty());
    }
}