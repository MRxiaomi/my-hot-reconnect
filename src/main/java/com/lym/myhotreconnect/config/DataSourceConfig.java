package com.lym.myhotreconnect.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.lym.myhotreconnect.service.event.ApolloConfigChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
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
    public DruidDataSource druidDataSource(){
        return dataSource();
    }

    /**
     * 数据库连接信息,从配置中心获取
     * @return
     */
    private DruidDataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName(config.getProperty("data.source.name","dataSourceDefaultName"));
        druidDataSource.setUrl(config.getProperty("data.source.url","dataSourceDefaultUrl"));
        druidDataSource.setUsername(config.getProperty("data.source.username","dataSourceDefaultUsername"));
        druidDataSource.setPassword(config.getProperty("data.source.password",""));
        druidDataSource.setDefaultReadOnly(Boolean.valueOf( config.getProperty("data.source.readonly","false")));
        return druidDataSource;
    }

    @EventListener
    public void refreshDataSource(ApolloConfigChangeEvent apolloConfigChangeEvent){
        for (String key : apolloConfigChangeEvent.getConfigChangeEvent().changedKeys()) {
            if (key.startsWith("data.source.")) {
                DruidDataSource druidDataSource = (DruidDataSource)applicationContext.getBean(MY_BEAN_DATA_SOURCE);
                druidDataSource.setName(config.getProperty("data.source.name","dataSourceDefaultName"));
                // 以下注释的参数不能重新设置
                //druidDataSource.setUrl(config.getProperty("data.source.url","dataSourceDefaultUrl"));
                //druidDataSource.setUsername(config.getProperty("data.source.username","dataSourceDefaultUsername"));
                //druidDataSource.setPassword(config.getProperty("data.source.password",""));
                druidDataSource.setDefaultReadOnly(Boolean.valueOf( config.getProperty("data.source.readonly","false")));
                System.out.println("【数据库】动态切换成功...");
                break;
            }
        }
    }

    /**
     * 动态数据源
     * @return
     */
    public class DynamicDataSource extends AbstractRoutingDataSource{
        /**
         * 执行SQL之前会执行该方法
         * @return
         */
        @Override
        protected Object determineCurrentLookupKey() {
            return MY_DATA_SOURCE_KEY;
        }
    }
}
