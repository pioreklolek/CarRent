package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;

public interface AuthService {



     User login(String login, String password);

     User register(String login, String password, String role);
}