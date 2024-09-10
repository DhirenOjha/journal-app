package com.project.journalApp.controller;

import com.project.journalApp.entity.User;
import com.project.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> displayUserEntries() {
        List<User> user=userService.displayAll();
        if(user!=null && !user.isEmpty()){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdminUser(@RequestBody User user){
        boolean created=false;
        created=userService.saveAdmin(user);
        if(created){
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(created, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteAdminUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        //User user=userService.findByUsername(username);
        boolean deleted=userService.deleteByUsername(username);
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
}
