package ru.practicum.shareit.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(UserDto user) {
        return userRepository.save(userMapper.fromDto(user));
    }

    public User updateUser(long id, UserDto userDto) {

        User user = userMapper.fromDto(userDto);

        return userRepository.findById(id).map(existingUser -> {
            boolean updated = false;

            if (user.getName() != null && !user.getName().isEmpty()) {
                existingUser.setName(user.getName());
                updated = true;
            }

            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                existingUser.setEmail(user.getEmail());
                updated = true;
            }
            if (updated) {
                return userRepository.save(existingUser);
            } else {
                return existingUser;
            }
        }).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));


    }

    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

}
