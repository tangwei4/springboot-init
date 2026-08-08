package com.springbootinit.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springbootinit.entity.User;
import com.springbootinit.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    // 默认使用 source 数据源
    @DS("source")
    public List<User> getUsersFromSource() {
        return userMapper.selectList(null);
    }

    // 切换到 target 数据源
    @DS("target")
    public List<User> getUsersFromTarget() {
        return userMapper.selectList(null);
    }

}