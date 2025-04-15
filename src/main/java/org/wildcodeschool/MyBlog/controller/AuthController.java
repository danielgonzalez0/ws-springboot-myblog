package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.user.UserLoginDTO;
import org.wildcodeschool.MyBlog.dto.user.UserRegistrationDTO;
import org.wildcodeschool.MyBlog.model.User;
import org.wildcodeschool.MyBlog.security.AuthentificationService;
import org.wildcodeschool.MyBlog.service.UserService;

import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthentificationService authentificationService;

    public AuthController(UserService userService, AuthentificationService authentificationService) {
        this.userService = userService;
        this.authentificationService = authentificationService;
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<User> register (@RequestBody UserRegistrationDTO userRegistrationDTO) {
        User registeredUser = userService.registerUser(
            userRegistrationDTO.getEmail(),
            userRegistrationDTO.getPassword(),
                Set.of("ROLE_USER")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLoginDTO userLoginDTO) {
        String token = authentificationService.authenticate(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        );
        return ResponseEntity.ok(token);
    }
}
