package org.example;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthService {
    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User login(String login, String password) {
        User user = userRepository.getUser(login);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    public User register(String login, String password, String role) {
        if (userRepository.getUser(login) != null) {
            System.out.println("Użytkownik o podanym loginie już istnieje!");
            return null;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(login, hashedPassword, role);
        userRepository.save(newUser);
        return newUser;
    }
}
