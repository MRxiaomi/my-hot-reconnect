package com.lym.myhotreconnect.service;

import com.lym.myhotreconnect.model.domain.User;

public interface UserService {
    User findUserById(Integer id);

    void save(User user);

    void testTranChangeDataSource(User user);

    void testTranChangeDataSourceReadOnly(User user);
}
