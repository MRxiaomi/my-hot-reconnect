package com.lym.myhotreconnect.mapper;

import com.lym.myhotreconnect.domain.User;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UserMapper extends tk.mybatis.mapper.common.BaseMapper<User>, MySqlMapper<User>, IdsMapper<User>, ConditionMapper<User>, ExampleMapper<User> {
}
