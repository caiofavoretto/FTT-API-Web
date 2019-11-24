package com.caiofavoretto.APIFTT.services;

import com.caiofavoretto.APIFTT.requests.PostRequest;
import org.springframework.http.ResponseEntity;

public interface PostService {
    public ResponseEntity list();

    public ResponseEntity getByUser(String userId);

    public ResponseEntity getById(String id);

    public ResponseEntity create(PostRequest request);

    public ResponseEntity update(PostRequest request);

    public ResponseEntity delete(String id);
}