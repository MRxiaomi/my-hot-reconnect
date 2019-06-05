package com.lym.myhotreconnect.listener;


import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.lym.myhotreconnect.service.event.ApolloConfigChangeEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class ApolloGlobalLister implements ApplicationEventPublisherAware {
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
}
