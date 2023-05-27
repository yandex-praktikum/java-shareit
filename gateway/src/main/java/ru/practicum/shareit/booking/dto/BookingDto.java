package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class BookingDto {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent
    private Date start;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent
    private Date end;

    private Integer itemId;
    private ItemDto item;
    private UserDto booker;
    private Integer bookerId;
    private String status;
}
