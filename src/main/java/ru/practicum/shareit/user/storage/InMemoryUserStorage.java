package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    UserMapper userMapper = new UserMapper();

    Map<Long, User> users = new HashMap<>();

    List<String> emailList = new ArrayList<>();

    private Long id = 1L;

    @Override
    public UserDto addUser(User user) {
        emailList.add(user.getEmail());
        user.setId(id);
        users.put(id, user);
        id++;
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        User updateUser = users.get(userId);
        if (user.getName() != null){
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null){
            emailList.remove(updateUser.getEmail());
            updateUser.setEmail(user.getEmail());
            emailList.add(user.getEmail());
        }
        users.put(updateUser.getId(), updateUser);
        return userMapper.toUserDto(updateUser);
    }

    @Override
    public List<UserDto> getUsers() {
        List <UserDto> userDtos = new ArrayList<>();
        for (User value : users.values()) {
            userDtos.add(userMapper.toUserDto(value));
        }
        return userDtos;
    }

    @Override
    public UserDto getUser(Long id) {
        return userMapper.toUserDto(users.get(id));
    }

    @Override
    public void deleteUser(Long id) {
        emailList.remove(users.get(id).getEmail());
        users.remove(id);
    }

    public List <String> getEmailList (){
        return emailList;
    }

    public List <Long> getUserId(){
        return new ArrayList<>(users.keySet());
    }

}
