package org.wildcodeschool.MyBlog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.exception.BadRequestException;
import org.wildcodeschool.MyBlog.model.User;
import org.wildcodeschool.MyBlog.repository.UserRepository;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser (String email, String password, Set<String> roles){
        if(userRepository.existsByEmail(email)){
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public Long getUserId(String email) {
        System.out.println("Recherche de l'ID pour l'email : " + email);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("Aucun utilisateur trouvé pour l'email : " + email);
            return null;
        }
        System.out.println("ID trouvé : " + user.getId());
        return user.getId();
    }
}
