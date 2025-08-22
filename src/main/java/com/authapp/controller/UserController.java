package com.authapp.controller;

import com.authapp.model.User;
import com.authapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/status")
    public ResponseEntity<String> updateUserStatus(Principal principal, @RequestParam boolean active) {
        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

            user.setActive(active);
            userRepository.save(user);

            String statusMessage = active ? "ativada" : "desativada";
            return ResponseEntity.ok("Conta" + statusMessage + "com sucesso!");
    }
}
