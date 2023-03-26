package ru.practicum.shareit.request;

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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
@Builder
public class ItemRequest {
    /**
     * id запроса в системе, уникальное
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long requestId;

    /**
     * пользователь, который создал запрос
     */
    @NotNull
    @JoinColumn(name = "request_author_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requestAuthor;

    /**
     * текст запроса
     */
    @NotBlank(message = "Текст запроса не может быть пустым")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "itemRequest")
    private List<Item> items;
}