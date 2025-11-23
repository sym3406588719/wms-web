package com.wms.msc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.msc.common.QueryPageParam;
import com.wms.msc.common.Result;
import com.wms.msc.entity.Goods;
import com.wms.msc.entity.Record;
import com.wms.msc.entity.RecordRes;
import com.wms.msc.service.GoodsService;
import com.wms.msc.service.RecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 * 商品出入库记录表 前端控制器
 * </p>
 *
 * @author wms
 * @since 2025-10-18
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        System.out.println("=== 开始查询 ===");

        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        System.out.println("前端查询参数:");
        System.out.println("name: '" + name + "'");
        System.out.println("goodstype: '" + goodstype + "'");
        System.out.println("storage: '" + storage + "'");

        Page<RecordRes> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();

        // 添加查询条件 - 使用正确的方式
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            // 使用apply方法，确保表别名正确
            queryWrapper.apply("b.name LIKE '%" + name + "%'");
            System.out.println("添加商品名称条件: " + name);
        }
        if (StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) {
            queryWrapper.apply("d.id = " + goodstype);
            System.out.println("添加分类条件: " + goodstype);
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)) {
            queryWrapper.apply("c.id = " + storage);
            System.out.println("添加仓库条件: " + storage);
        }

        System.out.println("最终查询条件: " + queryWrapper.getCustomSqlSegment());

        IPage<RecordRes> result = recordService.pageCC(page, queryWrapper);

        System.out.println("查询结果: " + result.getRecords().size() + " 条记录");
        System.out.println("=====================================");

        return Result.suc(result.getRecords(), result.getTotal());
    }
    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Goods goods=goodsService.getById(record.getGoodsId());

        int n=record.getCount();
        int num=goods.getCount()+n;
        goodsService.updateById(goods);
        goods.setCount(num);
        return recordService.save(record) ? Result.SUC() : Result.fail();
    }

}

