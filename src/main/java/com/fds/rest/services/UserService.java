package com.fds.rest.services;

import com.fds.rest.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    List<User> getAll();

    User save(User user);

    User update(User user);

    void deleteById(Long id);

    User findById(Long id);
}
