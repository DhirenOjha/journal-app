package com.project.journalApp.service;

import com.project.journalApp.entity.User;
import com.project.journalApp.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    private static final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();

    @Autowired
    private UserRepo userRepo;

    public boolean saveEntry(User user){
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }
    public boolean saveNewUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepo.save(user);
        return true;
    }
    public boolean saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepo.save(user);
        return true;
    }

    public List<User> displayAll(){
        return userRepo.findAll();
    }
    public Optional<User> displayById(ObjectId id){
        return userRepo.findById(id);
    }
//    public void deletebyID(ObjectId id){
//        userRepo.deleteById(id);
//    }
    public boolean deleteByUsername(String username){
        return userRepo.deleteByUsername(username);
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }
}
