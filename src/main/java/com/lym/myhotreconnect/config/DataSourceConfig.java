package com.lym.myhotreconnect.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.lym.myhotreconnect.service.event.ApolloConfigChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class DataSourceConfig{
    private static final String MY_DATA_SOURCE_KEY = "myDataSourceKey";
    private static final String MY_BEAN_DATA_SOURCE = "dataSource";

    @Resource
    private ApplicationContext applicationContext;
    @ApolloConfig
    private Config config;

    @Bean(MY_BEAN_DATA_SOURCE)
    public DynamicDataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(Collections.singletonMap(MY_DATA_SOURCE_KEY,dataSource()));
        return dynamicDataSource;
    }

    /**
     * 数据库连接信息,从配置中心获取
     * @return
     */
    private DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.close();
        druidDataSource.setName(config.getProperty("data.source.name","dataSourceDefaultName"));
        druidDataSource.setUrl(config.getProperty("data.source.url","dataSourceDefaultUrl"));
        druidDataSource.setUsername(config.getProperty("data.source.username","dataSourceDefaultUsername"));
        druidDataSource.setPassword(config.getProperty("data.source.password",""));
        druidDataSource.setDefaultReadOnly(Boolean.valueOf(config.getProperty("data.source.readonly","false")));
        return druidDataSource;
    }

    @EventListener
    public void refreshDataSource(ApolloConfigChangeEvent apolloConfigChangeEvent){
        for (String key : apolloConfigChangeEvent.getConfigChangeEvent().changedKeys()) {
            if (key.startsWith("data.source.")) {
                DataSourceConfig.DynamicDataSource dynamicDataSource = (DataSourceConfig.DynamicDataSource)applicationContext.getBean(MY_BEAN_DATA_SOURCE);
                dynamicDataSource.setTargetDataSources(Collections.singletonMap(MY_DATA_SOURCE_KEY,dataSource()));
                dynamicDataSource.afterPropertiesSet();
                System.out.println("【数据库】动态切换成功...");
                break;
            }
        }
    }

    /**
     * 动态数据源
     */
    public class DynamicDataSource extends AbstractRoutingDataSource{
        /**
         * 执行SQL之前会执行该方法
         */
        @Override
        protected Object determineCurrentLookupKey() {
            return MY_DATA_SOURCE_KEY;
        }
    }
}
