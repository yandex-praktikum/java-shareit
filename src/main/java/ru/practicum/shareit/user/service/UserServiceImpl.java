package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userDTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<UserDTO> getAll() {
        List <UserDTO> users = new ArrayList<>();
        for (User user : userRepository.getAll()) {
            users.add(UserMapper.toUserDTO(user));
        }
        return users;
    }

    public UserDTO getById(Long id) {
        return UserMapper.toUserDTO(userRepository.getById(id));
    }

    public UserDTO create(User user) {
        userCheck(user);
        return UserMapper.toUserDTO(userRepository.create(user));
    }


    public UserDTO update(User user, Long id) {
        userCheck(user);
        User user1 = userRepository.update(user,id);
        return UserMapper.toUserDTO(user1);
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    private void userCheck (User user){
        List<User> users = userRepository.getAll();
        for (User u : users){
            if(u.getEmail().equals(user.getEmail())){
            throw new DuplicateEmailException("Email Duplicate");
            }
        }

    }
}
