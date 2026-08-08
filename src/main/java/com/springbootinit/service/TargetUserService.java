package com.springbootinit.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springbootinit.entity.User;
import com.springbootinit.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@DS("target")
public class TargetUserService extends ServiceImpl<UserMapper, User> {
    // 所有方法默认使用 target
}