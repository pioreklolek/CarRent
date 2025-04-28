package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {

    private static final String USERS_JSON_PATH = "src/main/resources/JSONS/users.json";

    private List<User> users = new ArrayList<>();

    public UserRepository() {
        loadFromFile();
    }

    @Override
    public User getUser(String login) {
        Optional<User> user = users.stream().filter(u -> u.getLogin().equals(login)).findFirst();
        if (user.isPresent()) {
            System.out.println("Znaleziono użytkownika: " + user.get());
        } else {
            System.out.println("Nie znaleziono użytkownika o podanym loginie!");
        }
        return user.orElse(null);
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void removeUser(String login) {
        User user = getUser(login);
        if (user == null) {
            System.out.println("Użytkownik o podanym loginie nie istnieje!");
            return;
        }
        users.remove(user);
        saveToFile();
    }

    @Override
    public void addUser(User user) {
        if (getUser(user.getLogin()) != null) {
            System.out.println("Użytkownik o podanym loginie już istnieje!");
            return;
        }
        System.out.println("Dodano użytkownika: " + user + " !!");
        save(user);
        saveToFile();
    }

    private void loadFromFile() {
        File file = new File(USERS_JSON_PATH);
        if (!file.exists()) return;
        try {
            String json = Files.readString(file.toPath());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<User>>() {}.getType();
            users = gson.fromJson(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(USERS_JSON_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        users.add(user);
        saveToFile();
    }

    public User getUserById(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

}