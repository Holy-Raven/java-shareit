package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * TODO Sprint add-controllers.
 */


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper mapper;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {

        User user = mapper.returnUser(userDto);
        userService.addUser(user);
        return mapper.returnUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {

        User user = mapper.returnUser(userDto);
        return mapper.returnUserDto(userService.updateUser(user, userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {

        return mapper.returnUserDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {

        return userService.getAllUsers()
                .stream()
                .map(mapper::returnUserDto)
                .collect(toList());
   }
}
