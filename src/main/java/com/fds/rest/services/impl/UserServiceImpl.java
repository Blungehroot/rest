package com.fds.rest.services.impl;


import com.fds.rest.model.User;
import com.fds.rest.model.enums.Role;
import com.fds.rest.model.enums.Status;
import com.fds.rest.repository.UserRepository;
import com.fds.rest.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User with" + id)
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<User> existUser = userRepository.findByEmail(user.getEmail());
        if (existUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            user.setStatus(Status.ACTIVE);
            user.setName(user.getEmail().substring(0, user.getEmail().indexOf("@")));
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public User update(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User current = userRepository.getById(user.getId());
        current.setId(user.getId());
        current.setName(user.getName());
        current.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
