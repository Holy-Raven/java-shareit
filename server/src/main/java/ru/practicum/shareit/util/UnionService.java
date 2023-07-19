package ru.practicum.shareit.util;

public interface UnionService {

    void checkUser(Long userId);

    void checkItem(Long itemId);

    void checkBooking(Long booking);

    void checkRequest(Long requestId);
}
