package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class UserDto {

    long id;

    @NotNull(message = "Login cannot be empty or contain spaces.")
    @NotBlank(message = "Login cannot be empty or contain spaces.")
    String name;

    @NotNull(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must contain the character @")
    String email;

}
