package com.lym.myhotreconnect.service;

import com.lym.myhotreconnect.domain.User;

public interface UserService {
    User findUserById(Integer id);

    void save(User user);
}
