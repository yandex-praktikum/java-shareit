package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDTO UserToDto(User user) {
        return UserDTO.create(user.getId(),
                user.getEmail(),
                user.getName());
    }
}
