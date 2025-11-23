package com.wms.msc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goods;
import com.wms.msc.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.msc.entity.RecordRes;

/**
 * <p>
 * 商品出入库记录表 服务类
 * </p>
 *
 * @author wms
 * @since 2025-10-18
 */
public interface RecordService extends IService<Record> {
    IPage<RecordRes> pageCC(IPage<RecordRes> page, Wrapper<Record> wrapper);
}
