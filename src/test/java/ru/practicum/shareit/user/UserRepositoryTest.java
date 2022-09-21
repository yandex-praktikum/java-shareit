package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class UserRepositoryTest {

    User user = new User(null, "User111", "user111@mail.ru");
    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
    void testSaveUser() {
        assertThat(user.getId(), equalTo(null));
        userRepository.save(user);
        assertThat(user.getId(), notNullValue());
    }
}
