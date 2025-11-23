package com.wms.msc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goodstype;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
public interface GoodstypeService extends IService<Goodstype> {
    // 修正1：导入正确的 MyBatis-Plus Wrapper
    // 修正2：为 IPage 和 Wrapper 添加泛型 <Goodstype>，确保类型匹配
    IPage<Goodstype> pageCC(IPage<Goodstype> page, Wrapper<Goodstype> wrapper);
}
