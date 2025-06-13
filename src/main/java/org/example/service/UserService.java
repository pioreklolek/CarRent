package org.example.service;

import org.example.model.User;
import java.util.List;


public interface UserService {


     User findByLogin(String login);
     User findById(Long id);

     List<User> findAll();

    void save(User user);
     User createUser(String login, String password, String role);
     void deleteById(Long id);
     void deleteUserByLogin(String login);
     User updateUser(Long id, User userDetails);
}