package com.hua.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.hua.constant.MessageConstant;
import com.hua.entity.PageResult;
import com.hua.entity.QueryPageBean;
import com.hua.entity.Result;
import com.hua.pojo.CheckGroup;
import com.hua.service.CheckGroupService;
import org.aspectj.weaver.ast.Var;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.tools.tree.NegativeExpression;

import java.util.List;
import java.util.Map;

/**
 * @author ：hua
 * @date ：Created in 2019/6/11
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;

    /**
     * 获取检查组列表
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findAll")
    public PageResult findAll(@RequestBody QueryPageBean queryPageBean){
       return  checkGroupService.findAll(queryPageBean);
    }

    /**
     *
     * @param checkitemIds
     * @param checkGroup
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer[] checkitemIds , @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.add(checkitemIds,checkGroup);
            return  new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 删除检查组
     * @param id
     * @return
     */
    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            checkGroupService.delById(id);
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 数据回显：获取检查组
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL, "");
        }
    }

    /**
     * 获取检查组对应的检查项
     * @return
     */
    @RequestMapping("/findAssoicationCheckItem")
    public Result findAssoicationCheckItem(Integer id){
        try {
            List<Integer> checkItemcIds = checkGroupService.findAssoicationCheckItem(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemcIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL, "");
        }
    }

    /**
     * 更新检查组
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Map<String,Object> map){

        try {
            //反序列化
            //获取关联的检查项id
            JSONArray jsonArray = (JSONArray) map.get("checkitemIds");
            Integer[] checkitemIds = jsonArray.toArray(new Integer[]{});
            //获取检查组
            JSONObject jsonObject = (JSONObject)map.get("checkGroup");
            CheckGroup checkGroup = jsonObject.toJavaObject(CheckGroup.class);
            //更新检查项
            checkGroupService.update(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

}
