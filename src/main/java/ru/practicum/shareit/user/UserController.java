package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {

        log.info("Add User {} ", userDto.getId());
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {

        log.info("Update User {} ", userDto.getId());
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {

        log.info("User {} deleted ", userId);
        userService.deleteUser(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {

        log.info("Get User {} ", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {

        log.info("List all Users");
        return userService.getAllUsers();
   }
}
