package com.wms.msc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.msc.common.QueryPageParam;
import com.wms.msc.common.Result;
import com.wms.msc.entity.Menu;
import com.wms.msc.entity.Storage;
import com.wms.msc.entity.User;
import com.wms.msc.service.StorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/storage")
public class StorageController {
    // 注入的Service实例，应该通过这个实例调用方法
    @Autowired
    private StorageService storageService;

    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage) {
        // 错误：StorageService.save(...)
        // 正确：通过注入的实例调用
        return storageService.save(storage) ? Result.SUC() : Result.fail();
    }

    // 更新
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage) {
        // 错误：StorageService.updateById(...)
        return storageService.updateById(storage) ? Result.SUC() : Result.fail();
    }

    // 删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        // 错误：StorageService.removeById(...)
        return storageService.removeById(id) ? Result.SUC() : Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        System.out.println("num===" + query.getPageNum());
        System.out.println("size===" + query.getPageSize());
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        // 注意：这里应该使用Storage的分页，而不是User
        Page<Storage> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        // 查询条件应该针对Storage实体，而不是User
        LambdaQueryWrapper<Storage> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)) {
            queryWrapper.like(Storage::getName, name);
        }

        // 错误：StorageService.page(...)
        IPage<Storage> result = storageService.page(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
    @GetMapping("/list")
    public Result list() {
        List list = storageService.list();
        return Result.suc(list);
    }
    }
