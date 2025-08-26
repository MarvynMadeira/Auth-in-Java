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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register") 
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody UserRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário com email " + request.email() + " já existe");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(hashedPassword)
                .active(true)
                .build();

        return userRepository.save(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

