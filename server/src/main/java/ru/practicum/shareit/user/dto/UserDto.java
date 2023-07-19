package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class UserDto {

    private Long id;

    private String name;

    private String email;
}
