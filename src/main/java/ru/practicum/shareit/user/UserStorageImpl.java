package ru.practicum.shareit.user;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserStorageImpl implements UserStorage {

    static long newId = 1;

    @Getter
    private HashMap<Long, User> userMap = new HashMap<>();
    @Override
    public User get(long userid){
        if (userMap.containsKey(userid)) {
            return userMap.get(userid);
        } else {
            throw new RuntimeException("юзера нет в базе");
        }
    }
    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
    @Override
    public User add(User user) {

        for (User userCheckEmail : getAll()) {
            if (userCheckEmail.getEmail().equals(user.getEmail())){
                throw new RuntimeException("пользователь с таким емайл уже есть");
            }
        }

        if (user.getId() == 0) {
            user.setId(newId++);
        }

        userMap.put(user.getId(), user);
        return user;
    }
    @Override
    public User update(User user, long userId) {

        if (!userMap.containsKey(userId)){
            throw new RuntimeException("юзера нет в базе");
        }

        User newUser = userMap.get(userId);

        user.setId(userId);

        if (user.getName() != null) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null) {

            for (User userCheckEmail : getAll()) {
                if (userCheckEmail.getEmail().equals(user.getEmail()) && userCheckEmail.getId() != userId) {
                    throw new RuntimeException("пользователь с таким емайл уже есть");
                }
            }

            newUser.setEmail(user.getEmail());
        }

        userMap.put(userId, newUser);
        return userMap.get(user.getId());
    }
    @Override
    public void delete(User user){
        if (!userMap.containsValue(user)){
            throw new RuntimeException("юзера нет в базе");
        }        userMap.remove(user.getId());
    }

}

