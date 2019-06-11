package com.lym.myhotreconnect;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@RestController
@MapperScan(basePackages = "com.lym.myhotreconnect.mapper")
@EnableApolloConfig({"application","TEST2.GLOBAL.CONFIG"})
public class MyHotReconnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyHotReconnectApplication.class, args);
    }
}
