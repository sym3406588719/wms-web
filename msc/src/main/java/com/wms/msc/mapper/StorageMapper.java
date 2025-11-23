package com.wms.msc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper; // 改为MyBatis-Plus的Wrapper
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.msc.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 用于指定参数名

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@Mapper
public interface StorageMapper extends BaseMapper<Storage> {

    // 1. 修正Wrapper的包为MyBatis-Plus的
    // 2. 添加@Param注解指定参数名，与XML中的${wrapper.sqlSegment}对应
    IPage<Storage> pageCC(IPage<Storage> page, @Param("wrapper") Wrapper<Storage> wrapper);
}

