package com.project.journalApp.service;

import com.project.journalApp.entity.Journal;
import com.project.journalApp.entity.User;
import com.project.journalApp.repository.JournalRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalService {

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private UserService userService;

    public Journal saveEntry(Journal jour){
        return journalRepo.save(jour);
        //return true;
    }
    public List<Journal> displayAll(){
        return journalRepo.findAll();
    }
    public Optional<Journal> displayById(ObjectId id){
        return journalRepo.findById(id);
    }

    //@Transactional
    public void deletebyID(ObjectId id, String username){
        try {
            User user=userService.findByUsername(username);
            //boolean hasId=false;
            for(Journal u : user.getJournal()){
                if(u.getId().equals(id)){
                    user.getJournal().remove(u);
                    break;
                }
            }
            userService.saveEntry(user);
            journalRepo.deleteById(id);
        }
        catch (Exception e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
        //userService.saveEntry(user);
        //return null;
    }
}
