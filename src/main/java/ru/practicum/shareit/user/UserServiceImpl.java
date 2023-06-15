package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    UserRepository repository;
//
//    @Override
//    public User addUser(User user) {
//
//        for (User userCheckEmail : repository.getAll()) {
//            if (userCheckEmail.getEmail().equals(user.getEmail())) {
//                throw new EmailExistException("there is already a user with an email " + user.getEmail());
//            }
//        }
//
//        return repository.add(user);
//    }
//
//    @Override
//    public User updateUser(User user, long userId) {
//
//        repository.get(userId);
//        user.setId(userId);
//
//        User newUser = repository.get(userId);
//
//        if (user.getName() != null) {
//            newUser.setName(user.getName());
//        }
//
//        if (user.getEmail() != null) {
//            for (User userCheckEmail : repository.getAll()) {
//                if (userCheckEmail.getEmail().equals(user.getEmail()) && userCheckEmail.getId() != userId) {
//                    throw new EmailExistException("there is already a user with an email " + user.getEmail());
//                }
//            }
//
//            newUser.setEmail(user.getEmail());
//        }
//
//        return repository.update(newUser, userId);
//    }
//
//    @Override
//    public void deleteUser(long userId) {
//        repository.delete(repository.get(userId));
//    }
//
//    @Override
//    public User getUserById(long userId) {
//        return repository.get(userId);
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return repository.getAll();
//    }
//}


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public User addUser(User user) {

        for (User userCheckEmail : userRepository.findAll()) {
            if (userCheckEmail.getEmail().equals(user.getEmail())) {
                throw new EmailExistException("there is already a user with an email " + user.getEmail());
            }
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, long userId) {

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
        return userRepository.save(newUser);
    }

    @Override
    public void deleteUser(long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        }

        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(long userId) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(User.class, "User id " + userId + " not found.");
        } else {
            return userRepository.findById(userId).get();
        }

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}