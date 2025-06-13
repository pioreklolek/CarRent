package org.example.repository;

import org.example.model.User;

import java.util.List;

public interface UserRepository {
User save(User user);
User findByLogin(String login);
List<User> findAll();
User findById(Long id);

void delete(User user);

void deleteById(Long id);
}