package org.example.chatserice2.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.chatserice2.repository.RoleRepository;
import org.example.chatserice2.repository.UserRepository;
import org.example.chatserice2.utils.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager ;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder ;
    private final JWTUtils jwtUtils ;


    @PostMapping("/login")
    public R
}
