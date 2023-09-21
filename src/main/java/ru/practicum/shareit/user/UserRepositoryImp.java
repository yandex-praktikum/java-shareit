package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.MainData;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class UserRepositoryImp implements UserRepository {

    private final List<User> users;
    private final List<Item> items;
    private final List<ItemRequest> requests;
    private final List<Booking> bookings;

    private final List<Review> reviews;
    private Long globalId = 1L;

    public UserRepositoryImp(MainData mainData) {
        this.users = mainData.getUsers();
        this.items = mainData.getItems();
        this.requests = mainData.getRequests();
        this.bookings = mainData.getBookings();
        this.reviews = mainData.getReviews();
    }

    @Override
    public User add(User userToAdd) {
        if (containsEmail(userToAdd.getEmail())) {
            throw new ConflictException("email - " + userToAdd.getEmail() + ", уже занят");
        }
        userToAdd.setId(globalId);
        globalId++;
        users.add(userToAdd);
        return userToAdd;
    }

    @Override
    public User update(UserDto userToUpdate, Long userId) {
        containsById(userId);
        if (userToUpdate.getEmail() != null) {
            containsEmailForUpdate(userToUpdate.getEmail(), userId);
        }

        User oldUserToUpdate = getUser(userId);
        if (userToUpdate.getEmail() != null) {
            oldUserToUpdate.setEmail(userToUpdate.getEmail());
        }
        if (userToUpdate.getName() != null) {
            oldUserToUpdate.setName(userToUpdate.getName());
        }
        return oldUserToUpdate;
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void delete(Long userId) {
        users.removeIf(user -> user.getId().equals(userId));
        requests.removeIf(request -> {
            if (request.getRequesterId().equals(userId)) {
                for (Item item : items) {
                    if (request.getId().equals(item.getRequestId())) {
                        item.setRequestId(null);
                    }
                }
                return true;
            } else {
                return false;
            }
        });
        bookings.removeIf(booking -> booking.getBookerId().equals(userId));
        reviews.removeIf(review -> review.getClientId().equals(userId));
        for (Item item : items) {
            if (item.getOwnerId().equals(userId)) {
                delete(item.getId());
            }
        }
    }

    @Override
    public boolean containsEmail(String email) {
        for (User olduser : users) {
            if (email.equals(olduser.getEmail())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void containsEmailForUpdate(String email,Long userId) {
        users.stream().filter(user -> (email.equals(user.getEmail()) && !(user.getId().equals(userId)))).findFirst()
                .ifPresent((user) -> {throw new ConflictException("email - " + email + ", уже занят");});
    }

    @Override
    public User getUser(Long userId) {
        return users.stream().filter(user -> user.getId().equals(userId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("user по id - " + userId + " не найден"));
    }

    @Override
    public void containsById(Long userId) {
        users.stream().filter(user -> user.getId().equals(userId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("user по id - " + userId + " не найден"));
    }
}
