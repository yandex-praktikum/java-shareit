package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.base.model.BaseModel;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest implements BaseModel<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requestor", referencedColumnName = "id", nullable = false)
    private User requestor;

    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created;

    public ItemRequest(String description) {
        this.description = description;
    }
}
