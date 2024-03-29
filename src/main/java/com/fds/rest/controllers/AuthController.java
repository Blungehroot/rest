package com.fds.rest.controllers;

import com.fds.rest.exceptions.BadRequestException;
import com.fds.rest.model.User;
import com.fds.rest.model.dto.AuthenticationRequestDto;
import com.fds.rest.security.jwt.JwtTokenProvider;
import com.fds.rest.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private KafkaController kafkaController;


    public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Email address already in use.");
        }
        User current = userService.save(user);
        kafkaController.sendMessageToKafkaTopic("email=" + current.getEmail() + ", " + "firstName=" + current.getFirstName()
                + ", " + "lastName=" + current.getLastName());

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping(value = "login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        User user = userService.findByEmail(req.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not exist"));
        String token = jwtTokenProvider.createToken(req.getEmail(), user.getRole().name());
        Map<Object, Object> response = new HashMap<>();
        response.put("email", req.getEmail());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "logout")
    public void authenticate(HttpServletRequest req, HttpServletResponse resp) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(req, resp, null);
    }

    @GetMapping(value = "success")
    public ResponseEntity<?> successOauth2Signin() {
        return ResponseEntity.ok("Success sign in via google");
    }

}
