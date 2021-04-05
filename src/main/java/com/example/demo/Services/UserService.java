package com.example.demo.Services;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public boolean checkingExistingUsers(User user){
        return userRepository.findByLogin(user.getLogin()) != null;
    }

    @Transactional
    public void saveOneUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    //На самом деле он находит по логину!!!
    public User findUserByName(String name) {
        return userRepository.findByLogin(name);
    }

    @Transactional
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public void editProfile(User user, String login){
        User fromDb = findUserByName(login);
        fromDb.setLogin(user.getLogin());
        fromDb.setEmail(user.getEmail());
        fromDb.setPassword(passwordEncoder.encode(user.getPassword()));
        fromDb.setName(user.getName());
        fromDb.setSurname(user.getSurname());
        fromDb.setTelephone_number(user.getTelephone_number());
        userRepository.save(fromDb);
    }

}
