package com.springbootinit.service;

import com.springbootinit.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCompareService {

    @Autowired
    private SourceUserService sourceUserService;

    @Autowired
    private TargetUserService targetUserService;

    public void compareAndFix() {
        // 从源库读取
        List<User> sourceUsers = sourceUserService.list();
        // 从目标库读取
        List<User> targetUsers = targetUserService.list();
        // 对比并生成修复方案...
    }
}



