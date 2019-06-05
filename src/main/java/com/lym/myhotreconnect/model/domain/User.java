package com.lym.myhotreconnect.model.domain;

import javax.persistence.Table;

@Table(name = "user")
public class User {
    @javax.persistence.Id
    private Integer Id;

    private String name;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", name=" + name +
                '}';
    }
}
