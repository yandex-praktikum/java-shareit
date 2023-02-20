package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    @Column(name = "item_id")
    private Integer item;

    @Column(name = "author_id")
    private Integer author;

    private LocalDateTime created = LocalDateTime.now();
}
