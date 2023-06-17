package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
public class BookingDto {

    Long itemId;

    @NotNull(message = "start cannot be empty or contain spaces.")
    @FutureOrPresent(message = "start may be in the present or future")
    private LocalDateTime start;

    @NotNull(message = "end may be in the future")
    @Future
    private LocalDateTime end;

    private Status status;

}
