package com.wms.msc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.msc.common.QueryPageParam;
import com.wms.msc.common.Result;
import com.wms.msc.entity.Goods;  // 只使用Goods实体
import com.wms.msc.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 商品表 前端控制器
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        return goodsService.save(goods) ? Result.SUC() : Result.fail();
    }

    // 更新
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        return goodsService.updateById(goods) ? Result.SUC() : Result.fail();
    }

    // 删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return goodsService.removeById(id) ? Result.SUC() : Result.fail();
    }

    // 分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        System.out.println("num===" + query.getPageNum());
        System.out.println("size===" + query.getPageSize());
        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");

        Page<Goods> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            // 正确：使用 getter 方法
            queryWrapper.like(Goods::getName, name);
        }
        if (StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) {
            // 正确：使用 getter 方法 getGoodsType()
            // 注意：需要将字符串参数转换为 Integer 类型
            queryWrapper.eq(Goods::getGoodsType, Integer.parseInt(goodstype));
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)) {
            // 正确：使用 getter 方法 getStorage()
            // 注意：需要将字符串参数转换为 Integer 类型
            queryWrapper.eq(Goods::getStorage, Integer.parseInt(storage));
        }

        IPage<Goods> result = goodsService.page(page, queryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }
}
