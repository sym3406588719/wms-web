package com.wms.msc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.msc.entity.Storage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
public interface StorageService extends IService<Storage> {
    // 为Wrapper指定泛型为Storage，与操作的实体类保持一致
    IPage<Storage> pageCC(IPage<Storage> page, Wrapper<Storage> wrapper);
}

