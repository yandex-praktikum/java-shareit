package ru.practicum.shareit.userTest.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("Пётр Иванов");
        user.setEmail("some@email.com");

        service.createUser(user);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User savedUser = query.setParameter("email", user.getEmail())
                .getSingleResult();

        assertThat(savedUser.getId(), notNullValue());
        assertThat(savedUser.getName(), equalTo(user.getName()));
        assertThat(savedUser.getEmail(), equalTo(user.getEmail()));
        assertThat(savedUser.getRegistrationDate(), notNullValue());
    }

    @Test
    void getAllUsers() {
        User user1 = new User();
        user1.setName("Пётр Иванов");
        user1.setEmail("some@email.com");

        User user2 = new User();
        user2.setName("Пётр Иванов1");
        user2.setEmail("som1e@email.com");

        User user3 = new User();
        user3.setName("Пётр Иванов2");
        user3.setEmail("some2@email.com");

        service.createUser(user1);
        service.createUser(user2);
        service.createUser(user3);

        List<User> allUsers = service.getAllUsers();

        assertThat(allUsers, hasSize(3));
    }
}