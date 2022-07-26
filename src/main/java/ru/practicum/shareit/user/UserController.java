package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> listUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user){
        userService.addUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User userById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id){
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public User updateUser(@PathVariable int id, @Valid @RequestBody User user){
        return userService.updateUser(id, user);
    }

}
