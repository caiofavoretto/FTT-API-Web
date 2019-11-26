package com.caiofavoretto.APIFTT.controllers;

import com.caiofavoretto.APIFTT.requests.CommentRequest;
import com.caiofavoretto.APIFTT.requests.LikeRequest;
import com.caiofavoretto.APIFTT.responses.CommentResponse;
import com.caiofavoretto.APIFTT.responses.ErrorResponse;
import com.caiofavoretto.APIFTT.responses.LikeResponse;
import com.caiofavoretto.APIFTT.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("/{postId}")
    public ResponseEntity index(@PathVariable("postId") String postId) {
        return likeService.listByPost(postId);
    }

    @PostMapping("/{postId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity store(@PathVariable("postId") String postId, @RequestHeader("userId") String userId) {
        System.out.println(postId);
        System.out.println(userId);

        LikeRequest req = new LikeRequest();
        req.setUserId(userId);
        req.setPostId(postId);
        return likeService.create(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id, @RequestHeader("userId") String userId) {
        ResponseEntity response = likeService.getById(id);

        if(!(response.getBody() instanceof LikeResponse)) {
            return response;
        }

        if(!((CommentResponse)response.getBody()).getUserId().equals(userId)) {
            return new ResponseEntity<>(new ErrorResponse("Este usuário não tem permissão para remover o like."), HttpStatus.FORBIDDEN);
        }

        return likeService.delete(id);
    }
}
