package com.wms.msc.controller;

import com.wms.msc.common.Result;
import com.wms.msc.entity.Menu;
import com.wms.msc.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu") // 注意：前端调用时需包含这个路径（如 /menu/list）
public class MenuController {
    @Autowired
    private MenuService menuService;

    // 优化1：给 roleId 加默认值（比如默认查角色1的菜单），并允许空值
    @GetMapping("/list")
    public Result list(@RequestParam(required = false, defaultValue = "1") String roleId) {
        List<Menu> menuList;
        // 优化2：如果 roleId 为空或默认值，返回所有菜单（测试阶段方便调试）
        if (roleId == null || "1".equals(roleId)) {
            menuList = menuService.list(); // 查所有菜单，不限制角色
        } else {
            // 正常逻辑：按角色匹配菜单（menuRight 字段存角色ID，如 "1,2" 表示角色1和2可看）
            menuList = menuService.lambdaQuery()
                    .like(Menu::getMenuRight, roleId)
                    .list();
        }
        System.out.println("后端查询到的菜单数量：" + menuList.size()); // 打印数量，确认是否有数据
        return Result.suc(menuList);
    }
}
