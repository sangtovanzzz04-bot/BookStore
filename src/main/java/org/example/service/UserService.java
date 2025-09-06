package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        List<User> users = userRepository.getAllUser();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new RuntimeException("User not found");
    }
}
