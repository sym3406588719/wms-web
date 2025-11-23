package com.wms.msc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.msc.common.QueryPageParam;
import com.wms.msc.common.Result;
import com.wms.msc.entity.Goodstype;
import com.wms.msc.service.GoodstypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author wms
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService; // 注入的实例，用于调用非静态方法

    // 新增商品分类
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype) {
        return goodstypeService.save(goodstype) ? Result.SUC() : Result.fail();
    }

    // 更新商品分类
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype) {
        return goodstypeService.updateById(goodstype) ? Result.SUC() : Result.fail();
    }

    // 删除商品分类
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return goodstypeService.removeById(id) ? Result.SUC() : Result.fail();
    }

    // 分页查询商品分类（修正核心错误）
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        System.out.println("num===" + query.getPageNum());
        System.out.println("size===" + query.getPageSize());
        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");

        // 修正1：使用当前业务的实体类 Goodstype，而非 Storage
        Page<Goodstype> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        // 修正2：查询条件针对 Goodstype 实体
        LambdaQueryWrapper<Goodstype> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            queryWrapper.like(Goodstype::getName, name); // 条件字段与 Goodstype 对应
        }

        // 修正3：通过注入的实例调用 page 方法（非静态调用）
        IPage<Goodstype> result = goodstypeService.page(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
    @GetMapping("/list")
    public Result list() {
        List list = goodstypeService.list();
        return Result.suc(list);
    }
}
