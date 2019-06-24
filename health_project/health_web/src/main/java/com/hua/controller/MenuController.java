package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.entity.Result;
import com.hua.pojo.Menu;
import com.hua.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：hua
 * @date ：Created in 2019/6/24
 * @description ：菜单管理
 * @version: 1.0
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    MenuService menuService;

    /**
     * 获取菜单
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

            return menuService.findPage(queryPageBean);
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu = menuService.findById(id);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }

    }

    /**
     * 编辑数据
     * @param menu
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Menu menu){
        try {
            menuService.update(menu);
            return new Result(true, MessageConstant.UPDATE_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPDATE_MENU_FAIL);
        }
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            menuService.delById(id);
            return new Result(true, MessageConstant.DEL_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DEL_MENU_FAIL);
        }
    }

}
