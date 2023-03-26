package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {
    /**
     * id пользователя в системе, уникальное
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private long id;

    /**
     * имя пользователя
     */
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Column(nullable = false)
    private String name;

    /**
     * email пользователя, должен быть уникальным
     */
    @Email(message = "Электронная почта указана неверно")
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Column(nullable = false, unique = true)
    private String email;
}