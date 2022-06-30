package com.fds.rest.services;

import com.fds.rest.model.User;

import java.util.List;

public interface UserService {
    User getById(Long id);

    User findByName(String name);

    List<User> getAll();

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
