package com.wms.msc.service;

// 1. 删除Hutool的Wrapper导入（错误类型）
// import cn.hutool.db.sql.Wrapper;
// 2. 导入MyBatis-Plus正确的Wrapper类型
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.msc.entity.User;

public interface UserService extends IService<User> {
    // 保持与Mapper一致的返回值泛型
    IPage<User> pageC(IPage<User> page);

    // 3. 修正Wrapper类型，添加泛型<User>，与Mapper接口完全匹配
    IPage<User> pageCC(IPage<User> page, Wrapper<User> wrapper);
}
