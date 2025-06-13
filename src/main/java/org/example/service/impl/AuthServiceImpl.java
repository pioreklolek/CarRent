package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User login(String login, String password) {
        User user = userRepository.findByLogin(login);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    @Override
    public User register(String login, String password, String role) {
        if (userRepository.findByLogin(login) != null) {
            System.out.println("Użytkownik o podanym loginie już istnieje!");
            return null;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(login, hashedPassword, role);
        userRepository.save(newUser);
        return newUser;
    }

}
