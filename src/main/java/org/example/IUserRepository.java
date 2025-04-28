package org.example;

import java.util.List;

public interface IUserRepository {
    User getUser(String login);
    List<User> getUsers();
    void addUser(User user);
    void removeUser(String login);
    void save(User user);

    User getUserById(String userId);
}
