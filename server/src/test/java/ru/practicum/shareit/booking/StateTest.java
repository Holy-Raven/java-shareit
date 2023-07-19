package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StateTest {
    @Test
    void getEnumValue() {

        String stateStr = "Unknown";
        String finalStateStr = stateStr;
        assertThrows(UnsupportedStatusException.class, () -> State.getEnumValue(finalStateStr));

        stateStr = "ALL";
        State stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.ALL);

        stateStr = "CURRENT";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.CURRENT);

        stateStr = "PAST";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.PAST);

        stateStr = "FUTURE";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.FUTURE);

        stateStr = "REJECTED";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.REJECTED);

        stateStr = "WAITING";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.WAITING);
    }
}
