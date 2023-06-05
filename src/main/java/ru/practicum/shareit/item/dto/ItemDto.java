package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class ItemDto {

    long id;

    @NotNull(message = "Name cannot be empty or contain spaces.")
    @NotBlank(message = "Name cannot be empty or contain spaces.")
    String name;

    @NotNull(message = "Description cannot be empty or contain spaces.")
    @NotBlank(message = "Description cannot be empty or contain spaces.")
    String description;

    @NotNull(message = "Available cannot be empty")
    Boolean available;

}
