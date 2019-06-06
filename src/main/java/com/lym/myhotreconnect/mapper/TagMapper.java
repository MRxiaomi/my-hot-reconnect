package com.lym.myhotreconnect.mapper;

import com.lym.myhotreconnect.model.domain.Tag;
import com.lym.myhotreconnect.model.domain.User;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface TagMapper extends tk.mybatis.mapper.common.BaseMapper<Tag>, MySqlMapper<Tag>, IdsMapper<Tag>, ConditionMapper<Tag>, ExampleMapper<User> {

}
