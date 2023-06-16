package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    UserDto addUser(UserDto userDto);

    @Transactional
    UserDto updateUser(UserDto userDto, long userId);

    @Transactional
    void deleteUser(long userId);

    @Transactional(readOnly = true)
    UserDto getUserById(long userId);

    @Transactional(readOnly = true)
    List<UserDto> getAllUsers();
}
