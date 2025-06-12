package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.List;


public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User getByLogin(String login) {
        return repo.findByLogin(login);
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public void addUser(User user) {
        repo.save(user);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public void deleteUserByLogin(String login) {
        User user = getByLogin(login);
        if (user != null) {
            repo.delete(user);
        } else {
            throw new RuntimeException("Nie znaleziono użytkownika o podanym loginie");
        }
    }
}