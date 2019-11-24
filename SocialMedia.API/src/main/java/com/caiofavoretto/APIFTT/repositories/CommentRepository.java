package com.caiofavoretto.APIFTT.repositories;

import com.caiofavoretto.APIFTT.entities.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId);
}
