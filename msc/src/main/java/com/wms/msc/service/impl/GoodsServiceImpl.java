package com.wms.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goods;
import com.wms.msc.entity.Storage;
import com.wms.msc.mapper.GoodsMapper;
import com.wms.msc.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public IPage<Goods> pageCC(IPage<Goods> page, Wrapper<Goods> wrapper) {
        // 具体实现逻辑：调用mapper层的分页查询方法
        // 注意确保StorageMapper中存在对应的pageCC方法定义
        return baseMapper.pageCC(page, wrapper);
    }
}
