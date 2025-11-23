package com.wms.msc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goods;
import com.wms.msc.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.msc.entity.RecordRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品出入库记录表 Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2025-10-18
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    IPage<RecordRes> pageCC(IPage<RecordRes> page, @Param("ew") Wrapper<Record> wrapper);
}
