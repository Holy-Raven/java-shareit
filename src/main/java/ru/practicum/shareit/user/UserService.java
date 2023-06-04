package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    UserStorage userStorage;
    public User addUser(User user) {
        return userStorage.add(user);
    }

    public User updateUser(User user, long userId) {
        return userStorage.update(user, userId);
    }

    public void deleteUser(long userId) {
        userStorage.delete(userStorage.get(userId));
    }

    public User getUserById(long userId) {
    return userStorage.get(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }
}
