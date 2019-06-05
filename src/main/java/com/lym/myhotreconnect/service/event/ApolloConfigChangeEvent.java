package com.lym.myhotreconnect.service.event;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.springframework.context.ApplicationEvent;

public class ApolloConfigChangeEvent extends ApplicationEvent {
    private ConfigChangeEvent configChangeEvent;

    public ApolloConfigChangeEvent(Object source, ConfigChangeEvent configChangeEvent) {
        super(source);
        this.configChangeEvent = configChangeEvent;
    }

    public ConfigChangeEvent getConfigChangeEvent() {
        return configChangeEvent;
    }

    public void setConfigChangeEvent(ConfigChangeEvent configChangeEvent) {
        this.configChangeEvent = configChangeEvent;
    }
}
