package ru.practicum.shareit.constants;

public class MyConstants {
    public static final String ADD_USER_POINTCUT = "execution(public ru.practicum.shareit.user.dto.UserDto ru.practicum.shareit.user.dao.UserDaoImpl.addUser" +
            "(ru.practicum.shareit.user.User))";
    public static final String REMOVE_USER_POINTCUT = "execution(public String ru.practicum.shareit.user.dao.UserDaoImpl.removeUserById" +
            "(*))";

    public static final String GET_ALL_USERS_POINTCUT = "execution(public java.util.List<ru.practicum.shareit.user.dto.UserDto> " +
            "ru.practicum.shareit.user.dao.UserDaoImpl.getAllUsers())";

    public static final String UPDATE_USER_POINTCUT = "execution(public ru.practicum.shareit.user.dto.UserDto " +
            "ru.practicum.shareit.user.dao.UserDaoImpl.UpdateUser(..))";

    public static final String ADD_ITEM_POINTCUT = "execution(public ru.practicum.shareit.item.dto.ItemDto " +
            "ru.practicum.shareit.item.dao.ItemDaoImpl.addItem(..))";

    public static final String UPDATE_ITEM_POINTCUT = "execution(public ru.practicum.shareit.item.dto.ItemDto " +
            "ru.practicum.shareit.item.dao.ItemDaoImpl.updateItem(..))";

    public static final String GET_ITEM_BY_ID_POINTCUT = "execution(public ru.practicum.shareit.item.dto.ItemDto " +
            "ru.practicum.shareit.item.dao.ItemDaoImpl.getItemById(..))";

    public static final String GET_ALL_ITEM_POINTCUT = "execution(public java.util.List<ru.practicum.shareit.item.dto.ItemDto> " +
            "ru.practicum.shareit.item.dao.ItemDaoImpl.getAllItemForOwner(..))";

    public static final String SEARCH_ITEM_POINTCUT = "execution(public java.util.List<ru.practicum.shareit.item.dto.ItemDto> " +
            "ru.practicum.shareit.item.dao.ItemDaoImpl.searchItem(..))";
}
