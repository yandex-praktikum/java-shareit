package ru.practicum.shareit.utill;

public class NumberGenerator {
    private static long userId = 1;
    private static long itemId = 1;
    private static long itemRequestId = 1;
    private static long bookingId = 1;

    public static long getUserId() {
        return userId++;
    }

    public static long getItemId() {
        return itemId++;
    }

    public static long getItemRequestId() {
        return itemRequestId++;
    }

    public static long getBookingId() {
        return bookingId++;
    }
}
