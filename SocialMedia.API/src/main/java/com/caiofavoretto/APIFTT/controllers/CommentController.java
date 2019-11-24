package com.caiofavoretto.APIFTT.controllers;

import com.caiofavoretto.APIFTT.requests.CommentRequest;
import com.caiofavoretto.APIFTT.requests.UserRequest;
import com.caiofavoretto.APIFTT.responses.CommentResponse;
import com.caiofavoretto.APIFTT.responses.ErrorResponse;
import com.caiofavoretto.APIFTT.responses.UserResponse;
import com.caiofavoretto.APIFTT.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity index(@PathVariable("postId") String postId) {
        return commentService.listByPost(postId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity store(@Valid @RequestBody CommentRequest req) {
        return commentService.create(req);
    }

    @PutMapping("/{postId}/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @PathVariable("postId") String postId, @Valid @RequestBody CommentRequest req, @RequestHeader("userId") String userId) {
        req.setId(id);
        req.setUserId(userId);
        req.setPostId(postId);
        return commentService.update(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id, @RequestHeader("userId") String userId) {
        ResponseEntity response = commentService.getById(id);

        if(!(response.getBody() instanceof CommentResponse)) {
            return response;
        }

        if(!((CommentResponse)response.getBody()).getUserId().equals(userId)) {
            return new ResponseEntity<>(new ErrorResponse("Este usuário não tem permissão para excluir o comentário."), HttpStatus.FORBIDDEN);
        }

        return commentService.delete(id);
    }
}
