package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
