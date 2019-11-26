package com.caiofavoretto.APIFTT.repositories;

import com.caiofavoretto.APIFTT.entities.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> findByPostId(String postId);
}