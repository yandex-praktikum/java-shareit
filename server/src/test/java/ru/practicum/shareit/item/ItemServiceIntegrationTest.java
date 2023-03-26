package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
@SpringBootTest(
        properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    void getOwnedItemsListWithBookingsComments() {
        User owner = userService.create(new User(0, "owner name", "owner1@name.org"));

        Item item1 = itemService.create(
                new ItemCreateRequest("item1 name", "item1 description", true, 0),
                owner.getId());
        Item item2 = itemService.create(
                new ItemCreateRequest("item2 name", "item2 description", false, 0),
                owner.getId());

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        User booker = userService.create(new User(0, "booker name", "booker1@name.org"));
        Booking booking1 = bookingService.create(
                BookingCreateRequest.builder().itemId(item1.getId()).start(now.plusHours(1)).end(now.plusHours(2)).build(),
                booker.getId());
        bookingService.approve(booking1.getId(), true, owner.getId());

        Booking booking2 = bookingService.create(
                BookingCreateRequest.builder().itemId(item1.getId()).start(now.minusHours(2)).end(now.minusHours(1)).build(),
                booker.getId());
        bookingService.approve(booking2.getId(), true, owner.getId());

        Comment comment1 = itemService.createComment("comment1 item1", item1.getId(), booker.getId());

        List<Item> itemList = itemService.getOwnedItemsList(owner.getId(), 0, 20);

        assertEquals(itemList.get(0).getId(), item1.getId());
        assertEquals(itemList.get(1).getId(), item2.getId());

        assertEquals(itemList.get(0).getLastBooking().getId(), booking2.getId());
        assertEquals(itemList.get(0).getNextBooking().getId(), booking1.getId());
        assertNull(itemList.get(1).getLastBooking());
        assertNull(itemList.get(1).getNextBooking());

        assertEquals(comment1.getId(), itemList.get(0).getComments().get(0).getId());
        assertNull(itemList.get(1).getComments());
    }
}