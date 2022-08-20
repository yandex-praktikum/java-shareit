package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mopel.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Size(min = 1)
    String text;
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    Item item;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    User author;

}
