package com.wms.msc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.msc.entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    IPage<Goods> pageCC(IPage<Goods> page, @Param("wrapper") Wrapper<Goods> wrapper);
}
