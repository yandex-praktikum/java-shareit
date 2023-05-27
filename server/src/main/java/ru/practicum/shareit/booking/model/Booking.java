package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dateBegin;
    private Date dateEnd;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ID")
    private Item item;


    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User booker;

    private String status;


}
