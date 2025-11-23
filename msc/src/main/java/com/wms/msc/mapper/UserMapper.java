package com.wms.msc.mapper;

// 1. 删除 Hutool 的 Wrapper 导入（错误类型，直接删掉这行）
// import cn.hutool.db.sql.Wrapper;
// 2. 导入 MyBatis-Plus 正确的 Wrapper（必须带包名，避免和其他类混淆）
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wms.msc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<User> pageC(IPage<User> page);

    // 3. 修正参数类型：Wrapper 必须指定泛型 <User>，与实体类匹配
    IPage<User> pageCC(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);
}