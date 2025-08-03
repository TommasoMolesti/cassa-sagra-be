package com.tommasomolesti.cassa_sagra_be.controller;

import com.tommasomolesti.cassa_sagra_be.model.User;
import com.tommasomolesti.cassa_sagra_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public @ResponseBody Iterable<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            // TODO : gestire il messaggio di ritorno
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "")
    public @ResponseBody String addUser(@RequestBody User u) {
        userService.saveUser(u);
        // TODO : gestire il messaggio di ritorno
        return "User created";
    }

    @DeleteMapping(path = "{id}" )
    public @ResponseBody String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        // TODO : gestire il messaggio di ritorno
        return "User deleted";
    }
}


