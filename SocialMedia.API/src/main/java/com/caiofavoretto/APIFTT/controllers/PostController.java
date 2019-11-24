package com.caiofavoretto.APIFTT.controllers;

import com.caiofavoretto.APIFTT.requests.PostRequest;
import com.caiofavoretto.APIFTT.requests.UserRequest;
import com.caiofavoretto.APIFTT.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity index() {
        return postService.list();
    }

    @GetMapping("/{userId}")
    public  ResponseEntity listByUser(@PathVariable("userId") String userId) {
        return  postService.getByUser(userId);
    }

    @GetMapping("/{userId}/{id}")
    public  ResponseEntity get(@PathVariable("id") String id) {
        return  postService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity store(@Valid @RequestBody PostRequest req) {
        return postService.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @Valid @RequestBody PostRequest req) {
        req.setId(id);
        return postService.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return  postService.delete(id);
    }
}