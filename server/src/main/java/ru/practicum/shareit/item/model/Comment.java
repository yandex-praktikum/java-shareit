package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //@NotNull
    @Column(name = "text")
    private String text;
    @ManyToOne
    @ToString.Exclude
    private Item item;
    @ManyToOne
    @ToString.Exclude
    private User author;
    private LocalDateTime created;
}
