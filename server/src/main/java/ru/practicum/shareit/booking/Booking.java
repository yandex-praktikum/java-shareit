package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
@Builder
public class Booking {
    /**
     * id бронирования в системе, уникальное
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private long id;

    /**
     * вещь, которую бронируют
     */
    @NotNull
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    /**
     * пользователь, который бронирует
     */
    @NotNull
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User booker;

    /**
     * начало периода аренды, с даты
     */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime rentStartDate;

    /**
     * окончание периода аренды, по дату (включительно)
     */
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime rentEndDate;

    /**
     * подтверждение бронирования хозяином вещи
     */
    @Enumerated
    @Column(nullable = false)
    private BookingStatus status;
}