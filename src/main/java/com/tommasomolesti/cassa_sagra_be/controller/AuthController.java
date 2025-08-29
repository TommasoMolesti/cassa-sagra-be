package com.tommasomolesti.cassa_sagra_be.controller;

import com.tommasomolesti.cassa_sagra_be.dto.auth.LoginRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.auth.LoginResponseDTO;
import com.tommasomolesti.cassa_sagra_be.dto.auth.RegisterRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.user.UserResponseDTO;
import com.tommasomolesti.cassa_sagra_be.security.JwtService;
import com.tommasomolesti.cassa_sagra_be.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.tommasomolesti.cassa_sagra_be.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        UserResponseDTO registeredUser = userService.registerUser(registerRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtService.generateToken(userDetails);

        User authenticatedUser = (User) userDetails;

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                authenticatedUser.getId().toString(),
                authenticatedUser.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-user")
    public ResponseEntity<LoginResponseDTO> getUserFromToken(
        @AuthenticationPrincipal User currentUser,
        HttpServletRequest request
    ) {
        final String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        String id = currentUser.getId().toString();
        String email = currentUser.getEmail();

        LoginResponseDTO response = new LoginResponseDTO(id, email, token);

        return ResponseEntity.ok(response);
    }
}