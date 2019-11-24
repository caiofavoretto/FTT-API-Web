package com.caiofavoretto.APIFTT.services.Impl;

import com.caiofavoretto.APIFTT.entities.User;
import com.caiofavoretto.APIFTT.repositories.UserRepository;
import com.caiofavoretto.APIFTT.requests.UserRequest;
import com.caiofavoretto.APIFTT.responses.ErrorResponse;
import com.caiofavoretto.APIFTT.responses.InfoResponse;
import com.caiofavoretto.APIFTT.responses.UserResponse;
import com.caiofavoretto.APIFTT.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity list() {
        List<User> users = userRepository.findAll();

        if(users.size() == 0) {
            return new ResponseEntity<>(new ErrorResponse("Nenhum usuário cadastrado"),HttpStatus.NOT_FOUND);
        }

        ArrayList<UserResponse> response = new ArrayList<UserResponse>();

        users.forEach(u -> response.add(UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .build()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getById(String id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."),HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(UserResponse.builder()
                .id(user.get().getId())
                .name(user.get().getName())
                .lastName(user.get().getLastName())
                .email(user.get().getEmail())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity create(UserRequest request){
        User user = userRepository.findByEmail(request.getEmail());

        if(user != null) {
            return new ResponseEntity<>(new ErrorResponse("Usuário já existe."),HttpStatus.NOT_ACCEPTABLE);
        }

        User newUser = userRepository.save(
                User.builder()
                        .name(request.getName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .build()
        );

        return new ResponseEntity<>(UserResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity login(String email, String password){
        User user = userRepository.findByEmail(email);

        if(user == null) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."),HttpStatus.NOT_FOUND);
        }

        if(!user.getPassword().equals(password)) {
            return new ResponseEntity<>(new ErrorResponse("Senha inválida"), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(UserRequest request) {
        Optional<User> user = userRepository.findById(request.getId());

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."),HttpStatus.NOT_FOUND);
        }

        user.get().setName(request.getName());
        user.get().setLastName(request.getLastName());
        user.get().setEmail(request.getEmail());
        user.get().setPassword(request.getPassword());

        User newUser = userRepository.save(user.get());

        return new ResponseEntity<>(UserResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(String id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Usuário não encontrado."), HttpStatus.NOT_FOUND);
        }

        userRepository.deleteById(id);

        return new ResponseEntity<>(new InfoResponse("Usuário deletado."), HttpStatus.OK);
    }
}
