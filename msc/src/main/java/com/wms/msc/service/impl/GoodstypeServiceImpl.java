package com.wms.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goodstype;
import com.wms.msc.mapper.GoodstypeMapper;
import com.wms.msc.service.GoodstypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@Service
public class GoodstypeServiceImpl extends ServiceImpl<GoodstypeMapper, Goodstype> implements GoodstypeService {

    @Resource
    private GoodstypeMapper goodstypeMapper;

    // 修正：参数和返回值都与接口保持一致（统一为 Goodstype 实体）
    @Override
    public IPage<Goodstype> pageCC(IPage<Goodstype> page, Wrapper<Goodstype> wrapper) {
        // 1. 若使用 MyBatis-Plus 自带分页（无需自定义SQL），直接调用 baseMapper 的 selectPage 方法
        // return baseMapper.selectPage(page, wrapper);

        // 2. 若使用自定义分页逻辑（需在 GoodstypeMapper 中声明 pageCC 方法）
        return goodstypeMapper.pageCC(page, wrapper);
    }
}
