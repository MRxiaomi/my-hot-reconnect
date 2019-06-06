package com.lym.myhotreconnect.web;

import com.lym.myhotreconnect.model.domain.User;
import com.lym.myhotreconnect.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
public class MysqlUserController {
    @Resource
    private UserService userService;

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public String findUserById(@PathVariable("id") Integer id) {
        return Optional.ofNullable(userService.findUserById(id)).orElseGet(User::new).toString();
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    @GetMapping("/user/save")
    public String save(User user) {
        userService.save(user);
        return "save done!";
    }

    /**
     * 测试：事务进行过程中动态切换数据源，是否会造成影响
     *
     * @param user
     * @return
     */
    @GetMapping("/user/test-tran")
    public String testTran(User user) {
        System.out.println("MySql事务测试..." + System.currentTimeMillis());
        userService.testTranChangeDataSource(user);
        return "tran done!";
    }

    /**
     * 测试：事务进行过程中动态切换数据源只读配置，是否会造成影响
     *
     * @param user
     * @return
     */
    @GetMapping("/user/test-tran/read-only")
    public String testReadOnly(User user) {
        System.out.println("MySql事务测试ReadOnly..." + System.currentTimeMillis());
        userService.testTranChangeDataSourceReadOnly(user);
        return "tran done!";
    }
}
