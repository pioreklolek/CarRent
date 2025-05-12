package org.example.service;

import org.example.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String login, String password) {
        return userRepository.findByLogin(login)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                .orElse(null);
    }

    public User register(String login, String password, String role) {
        if (userRepository.findByLogin(login).isPresent()) {
            System.out.println("Użytkownik o podanym loginie już istnieje!");
            return null;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(login, hashedPassword, role);
        return userRepository.save(newUser);
    }
}