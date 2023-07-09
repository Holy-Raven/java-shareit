package ru.practicum.shareit.util;

import org.springframework.data.domain.PageRequest;

public interface UnionService {

    void checkUser(Long userId);

    void checkItem(Long itemId);

    void checkBooking(Long booking);

    void checkRequest(Long requestId);

    PageRequest checkPageSize(Integer from, Integer size);
}
