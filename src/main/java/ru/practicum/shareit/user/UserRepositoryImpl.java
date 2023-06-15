//package ru.practicum.shareit.user;
//
//import org.springframework.stereotype.Repository;
//import ru.practicum.shareit.exception.EmailExistException;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.user.model.User;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//@Repository
//public class UserRepositoryImpl implements UserRepository {
//
//    private static long newId = 1;
//
//    private final HashMap<Long, User> userMap = new HashMap<>();
//
//    @Override
//    public User get(long userId) {
//
//        if (userMap.containsKey(userId)) {
//            return userMap.get(userId);
//        } else {
//            throw new NotFoundException(User.class, "User id " + userId + " not found.");
//        }
//    }
//
//    @Override
//    public List<User> getAll() {
//        return new ArrayList<>(userMap.values());
//    }
//
//    @Override
//    public User add(User user) {
//
//        if (user.getId() == 0) {
//            user.setId(newId++);
//        }
//
//        userMap.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public User update(User user, long userId) {
//
//        userMap.put(userId, user);
//        return userMap.get(user.getId());
//    }
//
//    @Override
//    public void delete(User user) {
//        userMap.remove(user.getId());
//    }
//}