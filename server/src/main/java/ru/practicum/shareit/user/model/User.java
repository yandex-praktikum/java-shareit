package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public User() {

    }

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
