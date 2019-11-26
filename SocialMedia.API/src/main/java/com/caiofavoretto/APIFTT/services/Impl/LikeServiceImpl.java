package com.caiofavoretto.APIFTT.services.Impl;

import com.caiofavoretto.APIFTT.entities.Like;
import com.caiofavoretto.APIFTT.entities.Post;
import com.caiofavoretto.APIFTT.entities.User;
import com.caiofavoretto.APIFTT.repositories.LikeRepository;
import com.caiofavoretto.APIFTT.repositories.PostRepository;
import com.caiofavoretto.APIFTT.repositories.UserRepository;
import com.caiofavoretto.APIFTT.requests.LikeRequest;
import com.caiofavoretto.APIFTT.responses.*;
import com.caiofavoretto.APIFTT.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity listByPost(String postId) {
        Optional<Post> post = postRepository.findById(postId);

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado"), HttpStatus.NOT_FOUND);
        }

        List<Like> likes = likeRepository.findByPostId(postId);

        if(likes.size() == 0) {
            return new ResponseEntity<>(new InfoResponse("Nenhum like contabilizado"), HttpStatus.OK);
        }

        ArrayList<LikeResponse> response = new ArrayList<>();

        likes.forEach(c -> {
            Optional<User> user = userRepository.findById(c.getUserId());

            user.ifPresent(value -> response.add(LikeResponse.builder()
                    .id(c.getId())
                    .userId(c.getUserId())
                    .postId(c.getPostId())
                    .user(UserResponse.builder()
                            .id(value.getId())
                            .name(value.getName())
                            .lastName(value.getLastName())
                            .email(value.getEmail())
                            .colorHex(value.getColorHex())
                            .build())
                    .build()));
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getById(String postId, String userId) {
        List<Like> likes = likeRepository.findByPostId(postId);

        if(likes.size() < 1) {
            return new ResponseEntity<>(new ErrorResponse("Like não contabilizdo."), HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."), HttpStatus.FORBIDDEN);
        }

        AtomicReference<Like> likeData = new AtomicReference<>(new Like());

        likes.forEach(like -> {
            if(like.getUserId().equals(userId)) {
                likeData.set(like);
            }
        });

        return new ResponseEntity<>(LikeResponse.builder()
                .id(likeData.get().getId())
                .userId(likeData.get().getUserId())
                .postId(likeData.get().getPostId())
                .user(UserResponse.builder()
                        .id(user.get().getId())
                        .name(user.get().getName())
                        .lastName(user.get().getLastName())
                        .email(user.get().getEmail())
                        .colorHex(user.get().getColorHex())
                        .build())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity create(LikeRequest request){

        Optional<User> user = userRepository.findById(request.getUserId());

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."), HttpStatus.FORBIDDEN);
        }

        Optional<Post> post = postRepository.findById(request.getPostId());

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado."), HttpStatus.FORBIDDEN);
        }
        
        List<Like> likes = likeRepository.findByPostId(request.getPostId());

        AtomicBoolean likeExists = new AtomicBoolean(false);
        if(likes.size() > 0) {
           likes.forEach(like -> {
               if(like.getUserId().equals(user.get().getId())) {
                   likeExists.set(true);
               }
           });
        }

        if(likeExists.get()) {
            return new ResponseEntity<>(new ErrorResponse("Like já contabilizado"), HttpStatus.OK);
        }
        
        post.get().setLikes(post.get().getLikes() + 1);
        postRepository.save(post.get());

        Like newLike = likeRepository.save(
                Like.builder()
                        .userId(request.getUserId())
                        .postId(request.getPostId())
                        .build()
        );

        return new ResponseEntity<>(LikeResponse.builder()
                .id(newLike.getId())
                .userId(newLike.getUserId())
                .postId(newLike.getPostId())
                .user(UserResponse.builder()
                        .id(user.get().getId())
                        .name(user.get().getName())
                        .lastName(user.get().getLastName())
                        .email(user.get().getEmail())
                        .colorHex(user.get().getColorHex())
                        .build())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(String id) {
        Optional<Like> like = likeRepository.findById(id);

        if(!like.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Like não contabilizado."), HttpStatus.NOT_FOUND);
        }

        Optional<Post> post = postRepository.findById(like.get().getPostId());

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado."), HttpStatus.NOT_FOUND);
        }

        post.get().setLikes(post.get().getLikes() - 1);
        postRepository.save(post.get());

        likeRepository.deleteById(id);

        return new ResponseEntity<>(new InfoResponse("Like excluído."), HttpStatus.OK);
    }
}
