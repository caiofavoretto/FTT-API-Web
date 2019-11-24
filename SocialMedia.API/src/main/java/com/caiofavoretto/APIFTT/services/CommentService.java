package com.caiofavoretto.APIFTT.services;

import com.caiofavoretto.APIFTT.requests.CommentRequest;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    public ResponseEntity listByPost(String postId);

    public ResponseEntity getById(String id);

    public ResponseEntity create(CommentRequest request);

    public ResponseEntity update(CommentRequest request);

    public ResponseEntity delete(String id);
}