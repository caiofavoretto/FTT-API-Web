package com.caiofavoretto.APIFTT.services.Impl;

import com.caiofavoretto.APIFTT.entities.Like;
import com.caiofavoretto.APIFTT.entities.Post;
import com.caiofavoretto.APIFTT.entities.User;
import com.caiofavoretto.APIFTT.repositories.LikeRepository;
import com.caiofavoretto.APIFTT.repositories.PostRepository;
import com.caiofavoretto.APIFTT.repositories.UserRepository;
import com.caiofavoretto.APIFTT.requests.PostRequest;
import com.caiofavoretto.APIFTT.responses.ErrorResponse;
import com.caiofavoretto.APIFTT.responses.InfoResponse;
import com.caiofavoretto.APIFTT.responses.PostResponse;
import com.caiofavoretto.APIFTT.responses.UserResponse;
import com.caiofavoretto.APIFTT.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public ResponseEntity list(String userId) {
        List<Post> posts = postRepository.findAll();

        if(posts.size() == 0) {
            return new ResponseEntity<>(new ErrorResponse("Nenhum post encontrado"),HttpStatus.NOT_FOUND);
        }

        ArrayList<PostResponse> response = new ArrayList<>();

        posts.forEach(p -> {
            Optional<User> user = userRepository.findById(p.getUserId());

            List<Like> likes = likeRepository.findByPostId(p.getId());

            AtomicReference<Boolean> liked = new AtomicReference<>(false);
            likes.forEach(like -> {
                if(like.getUserId().equals(userId)){
                    liked.set(true);
                }
            });

            user.ifPresent(value -> response.add(PostResponse.builder()
                    .id(p.getId())
                    .userId(p.getUserId())
                    .title(p.getTitle())
                    .description(p.getDescription())
                    .likes(p.getLikes())
                    .comments(p.getComments())
                    .liked(liked.get())
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
    public ResponseEntity getByUser(String userId) {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado"),HttpStatus.NOT_FOUND);
        }

        List<Post> posts = postRepository.findByUserId(userId);

        if(posts.size() == 0) {
            return new ResponseEntity<>(new ErrorResponse("Nenhum post publicado"),HttpStatus.NOT_FOUND);
        }

        ArrayList<PostResponse> response = new ArrayList<>();

        posts.forEach(p -> response.add(PostResponse.builder()
                    .id(p.getId())
                    .userId(p.getUserId())
                    .title(p.getTitle())
                    .description(p.getDescription())
                    .likes(p.getLikes())
                    .comments(p.getComments())
                    .user(UserResponse.builder()
                            .id(user.get().getId())
                            .name(user.get().getName())
                            .lastName(user.get().getLastName())
                            .email(user.get().getEmail())
                            .colorHex(user.get().getColorHex())
                            .build())
                    .build()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getById(String id, String userId) {
        Optional<Post> post = postRepository.findById(id);

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado."), HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(post.get().getUserId());

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado"),HttpStatus.NOT_FOUND);
        }

        List<Like> likes = likeRepository.findByPostId(post.get().getId());

        AtomicReference<Boolean> liked = new AtomicReference<>(false);
        likes.forEach(like -> {
            if(like.getUserId().equals(userId)){
                liked.set(true);
            }
        });

        return new ResponseEntity<>(PostResponse.builder()
                .id(post.get().getId())
                .userId(post.get().getUserId())
                .title(post.get().getTitle())
                .description(post.get().getDescription())
                .likes(post.get().getLikes())
                .comments((post.get().getComments()))
                .liked(liked.get())
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
    public ResponseEntity create(PostRequest request){

        Optional<User> user = userRepository.findById(request.getUserId());

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."), HttpStatus.FORBIDDEN);
        }

        Post newPost = postRepository.save(
                Post.builder()
                        .userId(request.getUserId())
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .build()
        );

        return new ResponseEntity<>(PostResponse.builder()
                .id(newPost.getId())
                .userId(newPost.getUserId())
                .title(newPost.getTitle())
                .description(newPost.getDescription())
                .likes(newPost.getLikes())
                .comments(newPost.getComments())
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
    public ResponseEntity update(PostRequest request) {
        Optional<Post> post = postRepository.findById(request.getId());

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado."), HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(post.get().getUserId());

        if(!user.isPresent() || !user.get().getId().equals(request.getUserId())) {
            return new ResponseEntity<>(new ErrorResponse("Este usuário não tem permissão para alterar o post."), HttpStatus.FORBIDDEN);
        }

        post.get().setTitle(request.getTitle());
        post.get().setDescription(request.getDescription());

        Post newPost = postRepository.save(post.get());

        return new ResponseEntity<>(PostResponse.builder()
                .id(newPost.getId())
                .userId(newPost.getUserId())
                .title(newPost.getTitle())
                .description(newPost.getDescription())
                .likes(newPost.getLikes())
                .comments((newPost.getComments()))
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
        Optional<Post> post = postRepository.findById(id);

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado."), HttpStatus.NOT_FOUND);
        }

        postRepository.deleteById(id);

        return new ResponseEntity<>(new InfoResponse("Post excluído."), HttpStatus.OK);
    }
}
