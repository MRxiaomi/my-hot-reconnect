package com.lym.myhotreconnect.listener;


import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.lym.myhotreconnect.service.event.ApolloConfigChangeEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ApollConfigListener implements ApplicationEventPublisherAware {
    @Value("${data.source.name:defaultDataSource}")
    private String dataSource;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 监听配置中心配置更新
     *
     * @param changeEvent
     */
    @ApolloConfigChangeListener()
    public void onChange(ConfigChangeEvent changeEvent) {
        applicationEventPublisher.publishEvent(new ApolloConfigChangeEvent(this, changeEvent));
    }

    /**
     * 监听配置中心配置更新
     * 测试@value和namespace更新，@value更新需要结合@EnableApolloConfig({"application","TEST2.GLOBAL.CONFIG"})
     * @param changeEvent
     */
    @ApolloConfigChangeListener("TEST2.GLOBAL.CONFIG")
    public void OnGlobalChange(ConfigChangeEvent changeEvent) {
        //Config config = config.getProperty("data.source.name","dataSourceDefaultName");
        for (String key : changeEvent.changedKeys()) {
            if (key.startsWith("data.source.name")) {
                ConfigChange configChange = changeEvent.getChange("data.source.name");
                System.out.println("【数据库】切换后的GLOBAL数据源：" + configChange.getNewValue());
                break;
            }
        }
        applicationEventPublisher.publishEvent(new ApolloConfigChangeEvent(this, changeEvent));
    }

    @PostConstruct
    public void task(){
        new Thread(()->{
            while (true){
                System.out.println("【数据库】数据源打印定时任务：" + dataSource);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
