package com.fds.rest.services.impl;

import com.fds.rest.model.Role;
import com.fds.rest.model.User;
import com.fds.rest.repositories.RoleRepository;
import com.fds.rest.repositories.UserRepository;
import com.fds.rest.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoded;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoded) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoded = passwordEncoded;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        User existUser = userRepository.findByName(user.getName());

        if (existUser == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(userRole);
            user.setPassword(passwordEncoded.encode(user.getPassword()));
            user.setRoles(userRoles);
            user = userRepository.save(user);
        }

        return user;
    }

    @Override
    public User update(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        User current = userRepository.getById(user.getId());
        userRoles.add(userRole);
        current.setId(user.getId());
        current.setName(user.getName());
        current.setPassword(passwordEncoded.encode(user.getPassword()));
        current.setRoles(userRoles);
        return userRepository.save(current);
    }

    @Override
    public void deleteById(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }
}
