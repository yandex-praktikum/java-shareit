package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;
    ItemRepository itemRepository;
    ItemRequestRepository itemRequestRepository;
    BookingRepository bookingRepository;

    public UserService(UserRepository userRepository, ItemRepository itemRepository,
                       ItemRequestRepository itemRequestRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.bookingRepository = bookingRepository;
    }

    public User add(User userToAdd) {
        return userRepository.add(userToAdd);
    }

    public User update(UserDto userToUpdate, Long userId) {
        return userRepository.update(userToUpdate, userId);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User getOne(Long userId) {
        return userRepository.getUser(userId);
    }

    public void delete(Long userId) {
        userRepository.delete(userId);
    }
}
