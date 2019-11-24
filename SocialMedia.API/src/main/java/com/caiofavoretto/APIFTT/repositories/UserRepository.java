package com.caiofavoretto.APIFTT.repositories;

import com.caiofavoretto.APIFTT.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
