package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "start_date")
    LocalDateTime start;

    @Column(name = "end_date")
    LocalDateTime end;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 10)
    BookingStatus status;
}
