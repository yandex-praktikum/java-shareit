package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
