package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.base.model.BaseModel;
import ru.practicum.shareit.item.util.enums.ItemStatus;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
public class Item implements BaseModel<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request")
    private ItemRequest request;

    public Item(Long id, String name, String description, ItemStatus status, User owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.owner = owner;
        this.request = request;
    }

    public boolean isAvailable() {
        return status == ItemStatus.AVAILABLE;
    }
}
