package com.lym.myhotreconnect.web;

import com.lym.myhotreconnect.domain.User;
import com.lym.myhotreconnect.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/user/{id}")
    public String findUserById(@PathVariable("id") Integer id){
        return Optional.ofNullable(userService.findUserById(id)).orElseGet(User::new).toString();
    }

    @GetMapping("/user/save")
    public String save(User user){
        userService.save(user);
        return "save done";
    }
}
