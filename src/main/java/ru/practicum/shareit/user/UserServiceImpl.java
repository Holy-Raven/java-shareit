package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {

        User user = UserMapper.returnUser(userDto);

        userRepository.save(user);

        return UserMapper.returnUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {

        User user = UserMapper.returnUser(userDto);
        user.setId(userId);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        User newUser = userRepository.findById(userId).get();

        if (user.getName() != null) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            List<User> findEmail = userRepository.findByEmail(user.getEmail());

            if (!findEmail.isEmpty() && findEmail.get(0).getId() != userId) {
                throw new EmailExistException("there is already a user with an email " + user.getEmail());
            }
            newUser.setEmail(user.getEmail());
        }

        userRepository.save(newUser);

        return UserMapper.returnUserDto(newUser);
    }

    @Override
    public void deleteUser(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        } else {
            return UserMapper.returnUserDto(userRepository.findById(userId).get());
        }
    }

    @Override
    public List<UserDto> getAllUsers() {

        return UserMapper.returnUserDtoList(userRepository.findAll());
    }
}