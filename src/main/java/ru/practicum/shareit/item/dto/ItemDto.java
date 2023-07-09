package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {

    private Long id;

    @NotNull(message = "Name cannot be empty or contain spaces.")
    @NotBlank(message = "Name cannot be empty or contain spaces.")
    private String name;

    @NotNull(message = "Description cannot be empty")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Available cannot be empty")
    private Boolean available;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;

    @Positive(message = "must be positive")
    private Long requestId;
}
