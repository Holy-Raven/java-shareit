package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.UnsupportedStatusException;

public enum State {

    ALL,

    CURRENT,

    PAST,

    FUTURE,

    WAITING,

    REJECTED;

    public static State getEnumValue(String state) {

    try {
        return State.valueOf(state);
    } catch (Exception e) {
        throw new UnsupportedStatusException("Unknown state: " + state);
    }

    }
}
