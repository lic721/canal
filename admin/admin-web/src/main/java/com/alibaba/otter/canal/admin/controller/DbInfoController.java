package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.DbInfo;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.DbInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库信息控制层
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}")
public class DbInfoController {

    @Autowired
    DbInfoService dbInfoService;

    /**
     * 获取所有数据库信息列表(分页)
     *
     * @param dbInfo 筛选条件
     * @param env 环境变量
     * @return 数据库信息列表
     */
    @GetMapping(value = "/dbInfos")
    public BaseModel<Pager<DbInfo>> dbInfos(DbInfo dbInfo, Pager<DbInfo> pager,
                                                    @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.findList(dbInfo, pager));
    }


    /**
     * 获取所有数据库信息列表
     *
     * @param dbInfo 筛选条件
     * @param env 环境变量
     * @return 数据库信息列表
     */
    @GetMapping(value = "/allDbInfos")
    public BaseModel<List<DbInfo>> dbInfos(DbInfo dbInfo,
                                           @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.findAll(dbInfo));
    }

    /**
     * 保存数据库信息
     *
     * @param dbInfo 数据库信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/dbInfo")
    public BaseModel<String> save(@RequestBody DbInfo dbInfo, @PathVariable String env) {
        dbInfoService.save(dbInfo);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取数据库信息详情
     *
     * @param id 数据库信息id
     * @param env 环境变量
     * @return 检点信息
     */
    @GetMapping(value = "/dbInfo/{id}")
    public BaseModel<DbInfo> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.detail(id));
    }

    /**
     * 修改数据库信息
     *
     * @param dbInfo 数据库信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/dbInfo")
    public BaseModel<String> update(@RequestBody DbInfo dbInfo, @PathVariable String env) {
        dbInfoService.update(dbInfo);
        return BaseModel.getInstance("success");
    }

    /**
     * 删除数据库信息
     *
     * @param id 数据库信息id
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/dbInfo/{id}")
    public BaseModel<String> delete(@PathVariable Long id, @PathVariable String env) {
        dbInfoService.delete(id);
        return BaseModel.getInstance("success");
    }
}
