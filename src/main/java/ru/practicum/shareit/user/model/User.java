package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    private Long id;
    private String name;
    @Email
    private String email;
}
