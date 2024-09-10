package com.project.journalApp.controller;

import com.project.journalApp.entity.User;
import com.project.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //admin
//    @GetMapping
//    public List<User> displayUserEntries() {
//        return userService.displayAll();
//    }



    @DeleteMapping
    public ResponseEntity<?> deleteByUsername(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        userService.deleteByUsername(username);
//        userService.deletebyID(id);
        return new ResponseEntity<>(true,HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> updateEntry(@RequestBody User user){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User oldUser=userService.findByUsername(username);
        //if(oldUser!=null){
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
            //userService.saveEntry(oldUser);
        //}
        userService.saveEntry(oldUser);
        return new ResponseEntity<>(oldUser,HttpStatus.NO_CONTENT);
        //return oldUser;
    }
}
