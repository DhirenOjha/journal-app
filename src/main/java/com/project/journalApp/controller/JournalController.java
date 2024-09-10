package com.project.journalApp.controller;


import com.project.journalApp.entity.Journal;
import com.project.journalApp.entity.User;
import com.project.journalApp.service.JournalService;
import com.project.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalController {

    //controller -> service -> repo
    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getEntriesByUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        List<Journal> jour=user.getJournal();
        if(jour!=null)
            return new ResponseEntity<>(jour, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //@Transactional
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody Journal jour) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            jour.setDate(LocalDateTime.now());
            Journal journal = journalService.saveEntry(jour);
            User user = userService.findByUsername(username);
            List<Journal> j=new ArrayList<>();
            j.add(journal);
            //user.getJournal().add(journal);
            user.setJournal(j);
            userService.saveEntry(user);
            return new ResponseEntity<>(jour, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> displayById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user=userService.findByUsername(username);
        //System.out.println(user.getJournal());
        //boolean hasId=user.getJournal().contains(new List<Journal>(id));
        boolean hasId=false;
        for(Journal u : user.getJournal()){
            if(u.getId().equals(id)){
               hasId=true;
               break;
            }
        }
        if(hasId){
            Journal jour=journalService.displayById(id).orElse(null);
            return new ResponseEntity<>(jour, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteByID(@PathVariable ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user=userService.findByUsername(username);
        boolean hasId=false;
        for(Journal u : user.getJournal()){
            if(u.getId().equals(id)){
                hasId=true;
                break;
            }
        }
        if(hasId){
            journalService.deletebyID(id,username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody Journal entry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user=userService.findByUsername(username);
        boolean hasId=false;
        for(Journal u : user.getJournal()){
            if(u.getId().equals(id)){
                hasId=true;
                break;
            }
        }
        if(hasId){
            Journal oldEntry=journalService.displayById(id).orElse(null);
            if(oldEntry!=null) {
                oldEntry.setTitle(entry.getTitle() != null && !entry.getTitle().equals("") ? entry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(entry.getContent() != null && !entry.getContent().equals("") ? entry.getContent() : oldEntry.getContent());
            }
//        else
//            journalService.saveEntry(oldEntry);
            journalService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
