package com.wms.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Record;
import com.wms.msc.entity.RecordRes;
import com.wms.msc.mapper.RecordMapper;
import com.wms.msc.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 商品出入库记录表 服务实现类
 * </p>
 *
 * @author wms
 * @since 2025-10-18
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Resource
    private RecordMapper recordMapper;

    @Override
    public IPage<RecordRes> pageCC(IPage<RecordRes> page, Wrapper<Record> wrapper) {
        return baseMapper.pageCC(page, wrapper);
    }
    // 关键修正3：补全类的闭合大括号
}
