package com.caiofavoretto.APIFTT.repositories;

import com.caiofavoretto.APIFTT.entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId);
}
