package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoState;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.booking.BookingTestData.*;

public class BookingMapperTest {
    @Test
    void testToBookingDtoStatePast() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking1);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState1));
    }

    @Test
    void testToBookingDtoStateWaiting() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking2);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState2));
    }

    @Test
    void testToBookingDtoStateWaitingFuture() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking3);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState3));
    }

    @Test
    void testToBookingDtoStateWaitingCurrent() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking4);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState4));
    }

    @Test
    void testToBookingDtoStateWaitingRejected() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking5);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState5));
    }

    @Test
    void testToBookingDtoStateWaitingCancelled() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking6);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState6));
    }
}
