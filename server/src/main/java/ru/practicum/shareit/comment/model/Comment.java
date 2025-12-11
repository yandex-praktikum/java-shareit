package ru.practicum.shareit.comment.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
