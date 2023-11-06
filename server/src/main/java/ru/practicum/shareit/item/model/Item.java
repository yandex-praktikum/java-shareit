package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //@NotBlank
    private String name;
    //@NotNull
    private String description;
    //@NotNull
    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name = "owner_id")
    private Long ownerId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private ItemRequest request;

}
