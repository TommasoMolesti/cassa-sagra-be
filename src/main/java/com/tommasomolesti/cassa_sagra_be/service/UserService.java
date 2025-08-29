package com.tommasomolesti.cassa_sagra_be.service;

import com.tommasomolesti.cassa_sagra_be.dto.auth.RegisterRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.user.UserResponseDTO;
import com.tommasomolesti.cassa_sagra_be.exception.EmailAlreadyExistsException;
import com.tommasomolesti.cassa_sagra_be.exception.UserNotFoundException;
import com.tommasomolesti.cassa_sagra_be.mapper.UserMapper;
import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }

    public UserResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email '" + registerRequest.getEmail() + "' is already taken.");
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());

        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(newUser);

        return userMapper.toDTO(savedUser);
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return userMapper.toDTO(user);
    }

    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void deleteCurrentUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        userRepository.deleteById(userId);
    }
}
