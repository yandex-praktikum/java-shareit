package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.storage.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping
@AllArgsConstructor

public class UserController {
    @Autowired
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody @Valid User user) {
        log.info("Adding item " + user.getName());
        System.out.println("Add user controller");
        return userService.addUser(user);
    }

    @PatchMapping("/users/{userId}")
    public User updateUser(@RequestBody @Valid User user, @PathVariable int userId) {
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUserById(userId);
    }


}

//    @GetMapping("/films")
//    public List<Film> findAll() {
//        return filmService.findAllFilms();
//    }
//
//    @PostMapping(value = "/films")
//    public Film add(@RequestBody @Valid Film film) {
//        return filmService.addFilm(film);
//    }
//
//    @PutMapping(value = "/films")
//    public Film update(@RequestBody @Valid Film film) {
//        return filmService.updateFilm(film);
//    }
//
//    @PutMapping("/films/{id}/like/{userId}")
//    public ResponseEntity<Object> addLike(@PathVariable Integer id,
//                                          @PathVariable Integer userId) {
//        Film existingFilm = filmService.getFilmById(id);
//        if (existingFilm == null) {
//            ErrorResponse errorResponse = new ErrorResponse("Фильм с ID " + id + " не существует.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        }
//        try {
//            boolean added = filmService.addLike(id, userId);
//            if (added) {
//                return ResponseEntity.ok(existingFilm);
//            } else {
//                ErrorResponse errorResponse = new ErrorResponse("Пользователь с ID " + userId + " не найден.");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//            }
//        } catch (ValidationException ex) {
//            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        }
//    }
//
//    @DeleteMapping("/films/{id}/like/{userId}")
//    public ResponseEntity<Object> removeLike(@PathVariable Integer id,
//                                             @PathVariable Integer userId) {
//        Film existingFilm = filmService.getFilmById(id);
//        if (existingFilm == null) {
//            return ResponseEntity.notFound().build();
//        }
//        try {
//            boolean removed = filmService.removeLike(existingFilm, userId);
//            if (removed) {
//                return ResponseEntity.ok(existingFilm);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (ValidationException ex) {
//            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        }
//    }
//
//    @GetMapping("/films/popular")
//    public ResponseEntity<List<Film>> getTopTenFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
//        List<Film> popularFilms = filmService.getTopTenFilms(count);
//        if (popularFilms != null) {
//            return ResponseEntity.ok(popularFilms);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/films/{id}")
//    public ResponseEntity<Film> getFilm(@PathVariable Integer id) {
//        if (filmService.getFilmById(id) != null) {
//            return ResponseEntity.ok(filmService.getFilmById(id));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/genres/{id}")
//    public Genre getGenreById(@PathVariable int id) {
//        return filmService.getGenre(id);
//    }
//
//    @GetMapping("/genres")
//    public List<Genre> getGenres() {
//        return filmService.getAllGenres();
//    }
//
//    @GetMapping("/mpa/{id}")
//    public Mpa getMpa(@PathVariable int id) {
//        return filmService.getMpa(id);
//    }
//
//    @GetMapping("/mpa")
//    public List<Mpa> getAll() {
//        return filmService.getAllMpa();
//    }
//
//@ControllerAdvice
//public class CustomExceptionHandler {
//    @ExceptionHandler(ValidationException.class)
//    public ResponseStatusException handleValidationException(ValidationException ex) {
//        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
//    }
//
//}
//}

