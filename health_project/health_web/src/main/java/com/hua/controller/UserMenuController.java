package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hua.constant.MessageConstant;
import com.hua.entity.Result;
import com.hua.pojo.Menu;
import com.hua.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author ：XuNiuBi
 * @date ：Created in 2019/6/24
 * @version: 1.0
 */
@RestController
@RequestMapping("/menu")
public class UserMenuController {
    @Reference
    MenuService menuService;

    @RequestMapping("/getUserMenu")
    public Result getUserMenu(String username){
        try {
            List<Map<String,Object>> list = menuService.getUserMenu(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }

    }
}
