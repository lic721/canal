package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.DbInfo;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.DbInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据库信息控制层
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}/db")
public class DbInfoController {

    @Autowired
    DbInfoService dbInfoService;

    /**
     * 获取所有节点信息列表
     *
     * @param dbInfo 筛选条件
     * @param env 环境变量
     * @return 节点信息列表
     */
    @GetMapping(value = "/dbInfos")
    public BaseModel<Pager<DbInfo>> dbInfos(DbInfo dbInfo, Pager<DbInfo> pager,
                                                    @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.findList(dbInfo, pager));
    }

    /**
     * 保存节点信息
     *
     * @param dbInfo 节点信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/dbInfo")
    public BaseModel<String> save(@RequestBody DbInfo dbInfo, @PathVariable String env) {
        dbInfoService.save(dbInfo);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取节点信息详情
     *
     * @param id 节点信息id
     * @param env 环境变量
     * @return 检点信息
     */
    @GetMapping(value = "/dbInfo/{id}")
    public BaseModel<DbInfo> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.detail(id));
    }

    /**
     * 修改节点信息
     *
     * @param dbInfo 节点信息
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/dbInfo")
    public BaseModel<String> update(@RequestBody DbInfo dbInfo, @PathVariable String env) {
        dbInfoService.update(dbInfo);
        return BaseModel.getInstance("success");
    }

    /**
     * 删除节点信息
     *
     * @param id 节点信息id
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/dbInfo/{id}")
    public BaseModel<String> delete(@PathVariable Long id, @PathVariable String env) {
        dbInfoService.delete(id);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取远程节点运行状态
     *
     * @param ip 节点ip
     * @param port 节点端口
     * @param env 环境变量
     * @return 状态信息
     */
    @GetMapping(value = "/dbInfo/status")
    public BaseModel<Integer> status(@RequestParam String ip, @RequestParam Integer port, @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.remoteNodeStatus(ip, port));
    }

    /**
     * 启动远程节点
     *
     * @param id 节点id
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/dbInfo/start/{id}")
    public BaseModel<Boolean> start(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.remoteOperation(id, "start"));
    }

    /**
     * 获取远程节点日志
     *
     * @param id 节点id
     * @param env 环境变量
     * @return 节点日志
     */
    @GetMapping(value = "/dbInfo/log/{id}")
    public BaseModel<String> log(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.remoteCanalLog(id));
    }

    /**
     * 关闭远程节点
     *
     * @param id 节点id
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/dbInfo/stop/{id}")
    public BaseModel<Boolean> stop(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbInfoService.remoteOperation(id, "stop"));
    }
}
