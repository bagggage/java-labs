package com.example.lab.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.lab.dto.LogInDto;
import com.example.lab.dto.SignUpDto;
import com.example.lab.entity.User;
import com.example.lab.exceptions.InvalidParamsException;
import com.example.lab.exceptions.NotFoundException;
import com.example.lab.repository.UserRepository;

@Service
public class AuthService {
    private UserRepository repository;
    private UserService userService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public AuthService(UserRepository repository, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    private boolean isEmail(String string) {
        return string.contains("@");
    }

    public void logout() {
    }

    public String login(LogInDto loginDto) {
        String username =
            isEmail(loginDto.getLogin()) == false ?
            loginDto.getLogin() :
            repository.findByEmail(loginDto.getLogin())
            .orElseThrow(NotFoundException::new)
            .getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));

        UserDetails user = userService.userDetailsService().loadUserByUsername(username);

        return jwtService.generateToken((User)user);
    }

    public String signup(SignUpDto signupDto) {
        if (repository.existsByUsername(signupDto.getUsername()) ||
            repository.existsByEmail(signupDto.getEmail())) {
            throw new InvalidParamsException();
        }

        User user = new User();

        user.setUsername(signupDto.getUsername());
        user.setName(signupDto.getName());
        user.setEmail(signupDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(signupDto.getPassword()));

        user = userService.addUser(user);

        return jwtService.generateToken(user);
    }
}
