package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User get(long userid);

    List<User> getAll();

    User add(User user) ;

    User update(User user, long userId) ;

    void delete(User user);

}
