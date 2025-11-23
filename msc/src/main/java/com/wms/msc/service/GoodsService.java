package com.wms.msc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.msc.entity.Storage;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
public interface GoodsService extends IService<Goods> {
    IPage<Goods> pageCC(IPage<Goods> page, Wrapper<Goods> wrapper);
}
