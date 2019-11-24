package com.caiofavoretto.APIFTT.controllers;

import com.caiofavoretto.APIFTT.requests.LoginRequest;
import com.caiofavoretto.APIFTT.requests.UserRequest;
import com.caiofavoretto.APIFTT.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity index() {
        return userService.list();
    }

    @GetMapping("/{id}")
    public  ResponseEntity get(@PathVariable("id") String id) {
        return  userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity store(@Valid @RequestBody UserRequest req) {
        return userService.create(req);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest login) {
        return userService.login(login.getEmail(), login.getPassword());
    }

    @PutMapping
    public ResponseEntity update(@RequestHeader("userId") String userId, @Valid @RequestBody UserRequest req) {
        req.setId(userId);
        return userService.update(req);
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestHeader("userId") String userId) {
        return  userService.delete(userId);
    }
}
