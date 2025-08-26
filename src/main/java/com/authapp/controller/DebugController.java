package com.authapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {
    
    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> debugAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> authInfo = new HashMap<>();
        
        if (auth != null) {
            authInfo.put("principal", auth.getPrincipal().toString());
            authInfo.put("authorities", auth.getAuthorities().toString());
            authInfo.put("authenticated", auth.isAuthenticated());
            authInfo.put("name", auth.getName());
        } else {
            authInfo.put("message", "No authentication found");
        }
        
        return ResponseEntity.ok(authInfo);
    }
} 