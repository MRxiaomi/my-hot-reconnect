package com.lym.myhotreconnect;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@MapperScan(basePackages = "com.lym.myhotreconnect.mapper")
@EnableApolloConfig
public class MyHotReconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyHotReconnectApplication.class, args);
    }
}
