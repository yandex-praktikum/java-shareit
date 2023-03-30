package ru.practicum.shareit.user;

public class UserMapper {
    public UserDto toUserDto(User item) {
        return new UserDto(item.getId(),
                item.getName(),
                item.getEmail());
    }
}
