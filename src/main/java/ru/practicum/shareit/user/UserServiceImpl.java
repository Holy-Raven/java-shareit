package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userStorage;
    @Override
    public User addUser(User user) {
        return userStorage.add(user);
    }

    @Override
    public User updateUser(User user, long userId) {
        return userStorage.update(user, userId);
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.delete(userStorage.get(userId));
    }

    @Override
    public User getUserById(long userId) {
    return userStorage.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAll();
    }
}
