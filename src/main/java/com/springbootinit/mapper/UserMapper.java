package com.springbootinit.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springbootinit.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("source")
public interface UserMapper extends BaseMapper<User> {
    // 此 Mapper 的所有方法都使用 source 数据源
}