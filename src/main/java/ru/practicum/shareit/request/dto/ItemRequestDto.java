package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class ItemRequestDto {

    private long id;

    @NotNull(message = "Description cannot be empty")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    private Instant created;
}

