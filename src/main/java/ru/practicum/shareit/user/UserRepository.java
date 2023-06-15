package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
//
//    User get(long userid);
//
//    List<User> getAll();
//
//    User add(User user);
//
//    User update(User user, long userId);
//
//    void delete(User user);
}
