package com.lym.myhotreconnect.service;

import com.lym.myhotreconnect.model.domain.User;

public interface UserService {
    User findUserById(Integer id);

    void save(User user);
}
