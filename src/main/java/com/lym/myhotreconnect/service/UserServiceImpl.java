package com.lym.myhotreconnect.service;

import com.lym.myhotreconnect.mapper.UserMapper;
import com.lym.myhotreconnect.model.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserMapper userMapper;

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void save(User user) {
        userMapper.insertSelective(user);
    }
}
