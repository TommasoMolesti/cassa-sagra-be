package com.tommasomolesti.cassa_sagra_be.service;

import com.tommasomolesti.cassa_sagra_be.dto.UserRequestDTO;
import com.tommasomolesti.cassa_sagra_be.dto.UserResponseDTO;
import com.tommasomolesti.cassa_sagra_be.exception.EmailAlreadyExistsException;
import com.tommasomolesti.cassa_sagra_be.exception.UserNotFoundException;
import com.tommasomolesti.cassa_sagra_be.mapper.UserMapper;
import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).toList();
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A user with this email already exists : " + userRequestDTO.getEmail());
        }

        User userToSave = userMapper.toModel(userRequestDTO);
        User newUser = userRepository.save(userToSave);
        return userMapper.toDTO(newUser);
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
}
