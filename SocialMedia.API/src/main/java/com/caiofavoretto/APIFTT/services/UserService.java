package com.caiofavoretto.APIFTT.services;

import com.caiofavoretto.APIFTT.requests.UserRequest;
import com.caiofavoretto.APIFTT.responses.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity list();

    public ResponseEntity getById(String id);

    public ResponseEntity create(UserRequest request);

    public ResponseEntity login(String email, String password);

    public ResponseEntity update(UserRequest request);

    public ResponseEntity delete(String id);
}
