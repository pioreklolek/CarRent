package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository repo,PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User findByLogin(String login) {
        User user = repo.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("Nie znaleziono użytkownika: " + login);
        }
        return user;
    }
    @Override
    public User findById(Long id) {
        User user = repo.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Nie znaleziono użytkownika id: " + id);
        }
        return user;
    }
    @Override
    public List<User> findAll(){
        return repo.findAll();
    }
    @Override
    public void save(User user){
        repo.save(user);
    }
    @Override
    public User createUser(String login,String password, String role) {
        if (repo.findByLogin(login) != null) {
            throw new IllegalArgumentException("Użytkownik o podanym loginie już istnieje!");
        }
        String hashedPassword =  passwordEncoder.encode(password);
        User nUser = new User(login , hashedPassword, role.toLowerCase());
        return repo.save(nUser);
    }
    @Override
    public void deleteById(Long id){
        if (repo.findById(id) == null) {
            throw  new IllegalArgumentException("Użykownik o podanym ID:" + id + " nie istnieje!");
        }
        repo.deleteById(id);
    }
    @Override
    public void deleteUserByLogin(String login) {
        User user = repo.findByLogin(login);
        if (user == null) {
            throw  new IllegalArgumentException("Użykownik o podanym loginie:" + login + " nie istnieje!");
        }
        repo.delete(user);
    }
    @Override
    public User updateUser(Long id, User userDetails) {
     Optional<User> optionalUser = Optional.ofNullable(repo.findById(id));
     if (optionalUser.isPresent()) {
         User user = optionalUser.get();
         user.setLogin(userDetails.getLogin());
         if( userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()){
             user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
         }
         user.setRole(userDetails.getRole());
         return repo.save(user);
     }
     throw  new IllegalArgumentException("Użytkownik nie znaleziony!");
    }
}
