package com.caiofavoretto.APIFTT.services.Impl;

import com.caiofavoretto.APIFTT.entities.Comment;
import com.caiofavoretto.APIFTT.entities.Post;
import com.caiofavoretto.APIFTT.entities.User;
import com.caiofavoretto.APIFTT.repositories.CommentRepository;
import com.caiofavoretto.APIFTT.repositories.PostRepository;
import com.caiofavoretto.APIFTT.repositories.UserRepository;
import com.caiofavoretto.APIFTT.requests.CommentRequest;
import com.caiofavoretto.APIFTT.requests.PostRequest;
import com.caiofavoretto.APIFTT.responses.*;
import com.caiofavoretto.APIFTT.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

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

        List<Comment> comments = commentRepository.findByPostId(postId);

        if(comments.size() == 0) {
            return new ResponseEntity<>(new InfoResponse("Nenhum commentário disponível"), HttpStatus.OK);
        }

        ArrayList<CommentResponse> response = new ArrayList<>();

        comments.forEach(c -> {
            Optional<User> user = userRepository.findById(c.getUserId());

            user.ifPresent(value -> response.add(CommentResponse.builder()
                    .id(c.getId())
                    .userId(c.getUserId())
                    .postId(c.getPostId())
                    .description(c.getDescription())
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
    public ResponseEntity getById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if(!comment.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Comentário não encontrado."), HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(comment.get().getUserId());

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(CommentResponse.builder()
                .id(comment.get().getId())
                .userId(comment.get().getUserId())
                .postId(comment.get().getPostId())
                .description(comment.get().getDescription())
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
    public ResponseEntity create(CommentRequest request){

        Optional<User> user = userRepository.findById(request.getUserId());

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."), HttpStatus.FORBIDDEN);
        }

        Optional<Post> post = postRepository.findById(request.getPostId());

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não encontrado."), HttpStatus.FORBIDDEN);
        }

        post.get().setComments(post.get().getComments() + 1);
        postRepository.save(post.get());

        Comment newComment = commentRepository.save(
                Comment.builder()
                        .userId(request.getUserId())
                        .postId(request.getPostId())
                        .description(request.getDescription())
                        .build()
        );

        return new ResponseEntity<>(CommentResponse.builder()
                .id(newComment.getId())
                .userId(newComment.getUserId())
                .postId(newComment.getPostId())
                .description(newComment.getDescription())
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
    public ResponseEntity update(CommentRequest request) {
        Optional<Comment> comment = commentRepository.findById(request.getId());

        if(!comment.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Comentário não encontrado."), HttpStatus.NOT_FOUND);
        }

        Optional<User> user = userRepository.findById(comment.get().getUserId());

        if(!user.isPresent() || !user.get().getId().equals(request.getUserId())) {
            return new ResponseEntity<>(new ErrorResponse("Este usuário não tem permissão para alterar o comentário."), HttpStatus.FORBIDDEN);
        }

        Optional<Post> post = postRepository.findById(request.getPostId());

        if(!post.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Post não está mais disponível."), HttpStatus.NOT_FOUND);
        }

        post.get().setComments(post.get().getComments() - 1);
        postRepository.save(post.get());

        comment.get().setDescription(request.getDescription());

        Comment newComment = commentRepository.save(comment.get());

        return new ResponseEntity<>(CommentResponse.builder()
                .id(newComment.getId())
                .userId(newComment.getUserId())
                .postId(newComment.getPostId())
                .description(newComment.getDescription())
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
        Optional<Comment> comment = commentRepository.findById(id);

        if(!comment.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Comentário não encontrado."), HttpStatus.NOT_FOUND);
        }

        commentRepository.deleteById(id);

        return new ResponseEntity<>(new InfoResponse("Comentário excluído."), HttpStatus.OK);
    }
}
