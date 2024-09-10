package com.project.journalApp.repository;

import com.project.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo  extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
    boolean deleteByUsername(String username);
}

