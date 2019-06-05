package com.lym.myhotreconnect.web;

import com.lym.myhotreconnect.config.DataSourceConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ChangeDataSourceController {
    @Resource
    private DataSourceConfig dataSourceConfig;

    @GetMapping("/data-source/dynamic")
    @Deprecated
    public String change(){
        //dataSourceConfig.refreshDataSource();
        return "done!";
    }
}
