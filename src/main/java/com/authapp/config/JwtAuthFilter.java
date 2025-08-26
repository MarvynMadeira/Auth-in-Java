package com.authapp.config;

import com.authapp.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        
        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Request: " + method + " " + requestURI);
        System.out.println("Auth Header presente: " + (authHeader != null ? "SIM" : "NÃO"));

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Sem Bearer token - continuando...");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final Claims claims = jwtService.validateToken(jwt);
            
            System.out.println("Token validado: " + (claims != null ? "SIM" : "NÃO"));

            if(claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String userEmail = claims.getSubject();
                System.out.println("Email do usuário: " + userEmail);
                
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                System.out.println("UserDetails carregado: " + userDetails.getUsername());

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) claims.get("roles", List.class);
                System.out.println("Roles do token: " + roles);

                List<GrantedAuthority> authorities;

                if (roles == null || roles.isEmpty()) {
                    authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                    System.out.println("Nenhuma role encontrada - usando ROLE_USER padrão");
                } else {
                    authorities = roles.stream()
                        .map(role -> {
                            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                            System.out.println("Mapeando role: " + role + " -> " + authority);
                            return new SimpleGrantedAuthority(authority);
                        })
                        .collect(Collectors.toList());
                }
                
                System.out.println("Authorities finais: " + authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                System.out.println("Autenticação definida no SecurityContext");
            }
        } catch (Exception e) {
            System.out.println("Erro no processamento do JWT: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("========================");
        filterChain.doFilter(request, response);
    }
}