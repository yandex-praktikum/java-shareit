package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@Builder
public class Comment {
    /**
     * id отзыва, уникальное
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    /**
     * текст отзыва
     */
    @NotBlank(message = "Текст отзыва не может быть пустым")
    @Column(name = "comment_text", nullable = false)
    private String text;

    /**
     * вещь, на которую оставлен отзыв
     */
    @NotNull
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    /**
     * пользователь, который оставил отзыв
     */
    @NotNull
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    /**
     * дата создания отзыва
     */
    @NotNull
    @Column(nullable = false)
    private LocalDateTime created;
}