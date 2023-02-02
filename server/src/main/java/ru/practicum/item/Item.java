package ru.practicum.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.request.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "items")
@RequiredArgsConstructor
@AllArgsConstructor
@Jacksonized
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Positive
    @JoinColumn(name = "owner_id")
    private Long ownerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    @JsonBackReference
    private ItemRequest request;
}