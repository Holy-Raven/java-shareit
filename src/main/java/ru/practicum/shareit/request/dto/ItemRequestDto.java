package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class ItemRequestDto {

    @Positive
    private long id;

    @NotNull(message = "Description cannot be empty or contain spaces.")
    @NotBlank(message = "Name cannot be empty or contain spaces.")
    @Size(max = 200, message = "The maximum length of the description should not exceed 200 characters")
    private String description;

    @Positive
    @NotNull(message = "Description cannot be empty or contain spaces.")
    private long requestor;

    @NotNull
    @PastOrPresent
    private LocalDate created;

}

