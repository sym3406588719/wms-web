package com.wms.msc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Goodstype;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@Mapper
public interface GoodstypeMapper extends BaseMapper<Goodstype> {
    // 修正：@Param 参数名改为 "ew"，与 XML 中的 ${ew.sqlSegment} 对应
    IPage<Goodstype> pageCC(IPage<Goodstype> page, @Param("ew") Wrapper<Goodstype> wrapper);
}
