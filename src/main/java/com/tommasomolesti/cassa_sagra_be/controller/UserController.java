package com.tommasomolesti.cassa_sagra_be.controller;

import com.tommasomolesti.cassa_sagra_be.dto.UserRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.UserResponseDTO;
import com.tommasomolesti.cassa_sagra_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get Users")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userService.getUsers();
        return ResponseEntity.ok().body(users);
    }

//    @PostMapping
//    @Operation(summary = "Create a new User")
//    public ResponseEntity<UserResponseDTO> createUser(
//            @Validated({Default.class}) @RequestBody UserRequestDTO userRequestDTO
//    ) {
//        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
//        return ResponseEntity.ok().body(userResponseDTO);
//    }

    @PostMapping("/sync-profile")
    @Operation(summary = "Creates a local user profile after Supabase signup")
    public ResponseEntity<UserResponseDTO> syncUserProfile(@AuthenticationPrincipal Jwt jwt) {
        UserResponseDTO newUser = userService.createOrUpdateUserFromJwt(jwt);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a User by ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a User")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
