package org.example.repository;

import org.example.model.User;

import java.util.List;

public interface UserRepository {
void save(User user);
User findByLogin(String login);
List<User> findAll();
void delete(User user);

void deleteById(String id);
}