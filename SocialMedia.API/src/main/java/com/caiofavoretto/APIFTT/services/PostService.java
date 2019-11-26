package com.caiofavoretto.APIFTT.services;

import com.caiofavoretto.APIFTT.requests.PostRequest;
import org.springframework.http.ResponseEntity;

public interface PostService {
    public ResponseEntity list(String userId);

    public ResponseEntity getByUser(String userId);

    public ResponseEntity getById(String id, String userId);

    public ResponseEntity create(PostRequest request);

    public ResponseEntity update(PostRequest request);

    public ResponseEntity delete(String id);
}