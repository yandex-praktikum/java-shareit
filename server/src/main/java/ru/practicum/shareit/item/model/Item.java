package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
@Builder
public class Item {

    /**
     * id вещи в системе, уникальное
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private long id;

    /**
     * id пользователя-владельца
     */
    @Column(name = "owner_id", nullable = false)
    private long ownerId;

    /**
     * название вещи
     */
    @NotBlank(message = "Название вещи не может быть пустым")
    @Column(nullable = false)
    private String name;

    /**
     * описание вещи
     */
    @NotBlank(message = "Описание вещи не может быть пустым")
    @Column(nullable = false)
    private String description;

    /**
     * статус вещи
     * true - можно брать в аренду
     * false - нельзя брать в аренду
     */
    @NotNull
    @Column(nullable = false)
    private Boolean available;

    /**
     * предыдущее бронирование вещи
     */
    @Transient
    private Booking lastBooking;

    /**
     * следующее бронирование вещи
     */
    @Transient
    private Booking nextBooking;

    /**
     * список отзывов к вещи
     */
    @Transient
    private List<Comment> comments;

    /**
     * запрос, в ответ на который добавлена вещь
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest itemRequest;
}