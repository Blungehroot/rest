package com.fds.rest.controllers;

import com.fds.rest.dto.AuthentificationRequestDto;
import com.fds.rest.model.User;
import com.fds.rest.security.jwt.JwtTokenProvider;
import com.fds.rest.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/v1/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping(value = "registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok("User " + user.getName() + "successful registered");
    }

    @PostMapping(value = "login")
    public ResponseEntity login(@RequestBody AuthentificationRequestDto authentificationRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentificationRequestDto.getName(), authentificationRequestDto.getPassword()));
            User user = userService.findByName(authentificationRequestDto.getName());

            if (user == null) {
                throw new UsernameNotFoundException("User with name: " + user.getName() + "  not found");
            }

            String token = jwtTokenProvider.createToken(user.getName(), user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("name", user.getName());
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid name or password");
        }
    }
}
