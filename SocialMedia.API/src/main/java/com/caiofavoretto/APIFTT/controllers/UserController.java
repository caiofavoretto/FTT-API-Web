package com.caiofavoretto.APIFTT.controllers;

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

    @PostMapping("/{email}")
    public ResponseEntity login(@PathVariable("email") String email, @Param("password") String password) {
        return userService.login(email, password);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @Valid @RequestBody UserRequest req) {
        req.setId(id);
        return userService.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return  userService.delete(id);
    }
}
