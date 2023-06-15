package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto addUser(UserDto userDto) {

        User user = mapper.returnUser(userDto);

        for (User userCheckEmail : userRepository.findAll()) {
            if (userCheckEmail.getEmail().equals(user.getEmail())) {
                throw new EmailExistException("there is already a user with an email " + user.getEmail());
            }
        }

        userRepository.save(user);

        return mapper.returnUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {

        User user = mapper.returnUser(userDto);

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        user.setId(userId);

        User newUser = userRepository.findById(userId).get();

        if (user.getName() != null) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            for (User userCheckEmail : userRepository.findAll()) {
                if (userCheckEmail.getEmail().equals(user.getEmail()) && userCheckEmail.getId() != userId) {
                    throw new EmailExistException("there is already a user with an email " + user.getEmail());
                }
            }
            newUser.setEmail(user.getEmail());
        }

        userRepository.save(newUser);

        return mapper.returnUserDto(newUser);
    }

    @Override
    public void deleteUser(long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        } else {
            return mapper.returnUserDto(userRepository.findById(userId).get());
        }

    }

    @Override
    public List<UserDto> getAllUsers() {

        return mapper.returnUserDtoList(userRepository.findAll());
    }
}