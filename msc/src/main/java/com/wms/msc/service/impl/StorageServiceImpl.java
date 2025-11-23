package com.wms.msc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.msc.entity.Storage;
import com.wms.msc.mapper.StorageMapper;
import com.wms.msc.service.StorageService;
import org.springframework.stereotype.Service;

/**
 * Storage服务实现类
 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {

    // 实现接口中要求的pageCC方法
    @Override
    public IPage<Storage> pageCC(IPage<Storage> page, Wrapper<Storage> wrapper) {
        // 具体实现逻辑：调用mapper层的分页查询方法
        // 注意确保StorageMapper中存在对应的pageCC方法定义
        return baseMapper.pageCC(page, wrapper);
    }

    // 如果接口中还有其他未实现的抽象方法，也需要在此补充实现
    // 例如如果有单参数的pageCC方法，也需要实现：
    /*
    @Override
    public IPage<Storage> pageCC(IPage<Storage> page) {
        return baseMapper.pageCC(page);
    }
    */
}

