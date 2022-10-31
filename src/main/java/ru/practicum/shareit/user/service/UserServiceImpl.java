package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.WrongParameterException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.userDTO.UserDTO;

import java.security.InvalidParameterException;
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
        userDuplicateEmailCheck(user);
        userEmailCheck(user);
        userNameCheck(user);
        return UserMapper.toUserDTO(userRepository.create(user));
    }


    public UserDTO update(User user, Long id) {
        userDuplicateEmailCheck(user);
        User user1 = userRepository.getById(id);
        if (user.getName() != null){user1.setName(user.getName());}
        if (user.getEmail() != null){user1.setEmail(user.getEmail());}
        return UserMapper.toUserDTO(userRepository.update(user1));
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    private void userDuplicateEmailCheck (User user){
        List<User> users = userRepository.getAll();
        for (User u : users){
            if(u.getEmail().equals(user.getEmail())){
            throw new DuplicateEmailException("Email Duplicate");
            }
        }
    }
    private void userEmailCheck(User user){
        if (user.getEmail() == null){
            throw new WrongParameterException("Email = null!");
        }
    }
    private void userNameCheck(User user){
        if (user.getEmail() == null) {
            throw new WrongParameterException("Name = null!");
        }
    }
}
