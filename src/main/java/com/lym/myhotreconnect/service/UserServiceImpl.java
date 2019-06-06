package com.lym.myhotreconnect.service;

import com.lym.myhotreconnect.mapper.TagMapper;
import com.lym.myhotreconnect.mapper.UserMapper;
import com.lym.myhotreconnect.model.domain.Tag;
import com.lym.myhotreconnect.model.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserMapper userMapper;

    @Resource
    private TagMapper tagMapper;

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void save(User user) {
        userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public void saveAndUpdate(User user) {
        userMapper.insertSelective(user);

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //这里切换为只读模式，看看事务是否能执行成功
        //预期：事务成功，不报异常
        //结果：事务成功，不报异常
        Tag tag  = new Tag();
        tag.setName(user.getName());
        tagMapper.insertSelective(tag);
    }
}
