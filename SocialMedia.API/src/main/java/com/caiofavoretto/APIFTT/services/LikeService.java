package com.caiofavoretto.APIFTT.services;

import com.caiofavoretto.APIFTT.requests.LikeRequest;
import org.springframework.http.ResponseEntity;

public interface LikeService {
    public ResponseEntity listByPost(String postId);

    public ResponseEntity getById(String id);

    public ResponseEntity create(LikeRequest request);

    public ResponseEntity delete(String id);
}