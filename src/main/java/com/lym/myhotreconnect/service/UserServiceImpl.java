package com.lym.myhotreconnect.service;

import com.lym.myhotreconnect.mapper.TagMapper;
import com.lym.myhotreconnect.mapper.UserMapper;
import com.lym.myhotreconnect.model.domain.Tag;
import com.lym.myhotreconnect.model.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private TagMapper tagMapper;

    private AtomicLong atomicLong = new AtomicLong(0);

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
    public void testTranChangeDataSource(User user) {
        userMapper.insertSelective(user);

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //这里切换为只读模式，看看事务是否能执行成功
        //预期：事务成功，不报异常
        //结果：事务成功，不报异常
        Tag tag = new Tag();
        tag.setName(user.getName());
        tagMapper.insertSelective(tag);
    }

    @Override
    @Transactional
    public void testTranChangeDataSourceReadOnly(User user) {
        //测试：为用户名和标签名生成后缀，然后切换为只读模式
        //预期：用户名和标签名一一对应
        //结果：使用@Transactional情况下，用户名和标签名一一对应
        //     不使用@Transactional情况下，标签记录会缺失一部分
        //     因此使用@Transactional情况下动态切换是安全的
        user.setName(user.getName() + atomicLong.incrementAndGet());
        userMapper.insertSelective(user);

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 捕获异常作用：用于验证，切换后，如果还是同一个数据源配置的话，不会出现异常
         *                           如果不是同一个数据源配置的话，会抛出SQLException
         */
        try {
            Tag tag = new Tag();
            tag.setName(user.getName());
            tagMapper.insertSelective(tag);
        } catch (Exception e) {
            System.out.println("执行错误。。。。。。");
            //退出程序，方便查看日志
            System.exit(0);
        }
    }
}
