package com.caiofavoretto.APIFTT.controllers;

import com.caiofavoretto.APIFTT.requests.PostRequest;
import com.caiofavoretto.APIFTT.responses.ErrorResponse;
import com.caiofavoretto.APIFTT.responses.PostResponse;
import com.caiofavoretto.APIFTT.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity index(@RequestHeader String userId) {
        return postService.list(userId);
    }

    @GetMapping("/user")
    public ResponseEntity listByUser(@RequestHeader("userId") String userId) {
        return  postService.getByUser(userId);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity get(@PathVariable("id") String id, @RequestHeader("userId") String userId) {
        return postService.getById(id, userId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity store(@Valid @RequestBody PostRequest req, @RequestHeader String userId) {
        req.setUserId(userId);
        return postService.create(req);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @Valid @RequestBody PostRequest req, @RequestHeader String userId) {
        req.setId(id);
        req.setUserId(userId);
        return postService.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id, @RequestHeader("userId") String userId) {
        ResponseEntity response = postService.getById(id, userId);

        if(!(response.getBody() instanceof PostResponse)) {
            return response;
        }

        if(!((PostResponse)response.getBody()).getUserId().equals(userId)) {
            return new ResponseEntity<>(new ErrorResponse("Este usuário não tem permissão para excluir o post."), HttpStatus.FORBIDDEN);
        }

        return postService.delete(id);
    }
}