package com.caiofavoretto.APIFTT.repositories;

import com.caiofavoretto.APIFTT.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
