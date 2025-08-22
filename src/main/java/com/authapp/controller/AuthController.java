package com.authapp.controller;

import com.authapp.dto.AuthRequest;
import com.authapp.dto.AuthResponse;
import com.authapp.dto.UserRequest;
import com.authapp.model.User;
import com.authapp.repository.UserRepository;
import com.authapp.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;


    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/registerNow")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody UserRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário com email " + request.email() + " já existe");
        }

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .active(true)
                .build();

        return userRepository.save(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas..."));

            if(!user.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponse("Conta desativada."));
            }
            
            if(!request.password().equals(user.getPassword())) {
                throw new RuntimeException("Credenciais inválidas.");
            }

            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token));
    }
}

