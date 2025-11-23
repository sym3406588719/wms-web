package com.wms.msc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.msc.common.QueryPageParam;
import com.wms.msc.common.Result;
import com.wms.msc.entity.Menu;
import com.wms.msc.entity.User;
import com.wms.msc.service.MenuService;
import com.wms.msc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;


    @GetMapping("/list")
    public List<User> list() {
        return userService.list();}
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
      List list=userService.lambdaQuery().eq(User::getNo,no).list();
      return list.size()>0?Result.suc(list):Result.fail();
    }

    @GetMapping("/findById")
    public Result findById(@RequestParam String no) {
        List list=userService.lambdaQuery().eq(User::getNo,no).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return userService.save(user)?Result.SUC():Result.fail();}
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        return userService.updateById(user)?Result.SUC():Result.fail();}
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return userService.removeById(id)?Result.SUC():Result.fail();}
    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List list=userService.lambdaQuery().eq(User::getNo,user.getNo()).eq(User::getPassword,user.getPassword()).list();
        if (list.size()>0){
            User user1=(User)list.get(0);
            List menuList=menuService.lambdaQuery().like(Menu::getMenuRight,user1.getRoleId()).list();
            HashMap res=new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.suc(res);
        }
        return Result.fail();}
    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);}
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user);}
    //删除
    @GetMapping("/delete")
    public boolean delete (Integer id) {
        return userService.removeById(id);}
    //查询（模糊，匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody User user) {
        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(user.getName())){
            LambdaQueryWrapper.like(User::getName, user.getName());
        }

        return Result.suc(userService.list(LambdaQueryWrapper));}
    @PostMapping("/listPage")
    public List<User> listPage(@RequestBody QueryPageParam query) {
        System.out.println("num==="+query.getPageNum());
        System.out.println("size==="+query.getPageSize());
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        System.out.println("name==="+(String) param.get("name"));
        //System.out.println("no==="+(String) param.get("no"));
        /* LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(User::getName, user.getName());
        return userService.list(LambdaQueryWrapper);*/

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(User::getName,name);
        IPage result=userService.page(page,LambdaQueryWrapper);
        System.out.println("total=="+result.getTotal());
        return result.getRecords();
    }
    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        System.out.println("num===" + query.getPageNum());
        System.out.println("size===" + query.getPageSize());
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");
        //System.out.println("no==="+(String) param.get("no"));
        /* LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(User::getName, user.getName());
        return userService.list(LambdaQueryWrapper);*/

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)) {
            LambdaQueryWrapper.like(User::getName, name);
        }
        // 修复性别筛选条件判断错误（原代码误将sex判断写成name）
        if(StringUtils.isNotBlank(sex) && !"null".equals(sex)){
            LambdaQueryWrapper.eq(User::getSex,sex);
        }
        if(StringUtils.isNotBlank(roleId) && !"null".equals(sex)){
            LambdaQueryWrapper.eq(User::getRoleId,roleId);
        }

        IPage result = userService.page(page, LambdaQueryWrapper);
        System.out.println("total==" + result.getTotal());
        return Result.suc(result.getRecords(),result.getTotal());
    }
}
