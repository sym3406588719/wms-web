package com.wms.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.msc.entity.User;
import com.wms.msc.mapper.UserMapper;
import com.wms.msc.service.UserService;
import org.springframework.stereotype.Service;

@Service
// 注意：ServiceImpl的泛型需指定正确的Mapper和Entity
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 实现pageC方法
    @Override
    public IPage<User> pageC(IPage<User> page) {
        // 调用Mapper层的pageC方法（需确保UserMapper有该方法）
        return baseMapper.pageC(page);
    }

    // 实现pageCC方法：参数类型必须与接口完全一致（IPage<User>和Wrapper<User>）
    @Override
    public IPage<User> pageCC(IPage<User> page, Wrapper<User> wrapper) {
        // 调用Mapper层的pageCC方法，传入正确类型的参数
        return baseMapper.pageCC(page, wrapper);
    }
}

