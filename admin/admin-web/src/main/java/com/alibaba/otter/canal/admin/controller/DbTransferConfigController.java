package com.alibaba.otter.canal.admin.controller;

import com.alibaba.otter.canal.admin.dto.DbTransferConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.DbTransferConfig;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.DbTransferConfigService;

/**
 * 扩容配置
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}")
public class DbTransferConfigController {

    @Autowired
    DbTransferConfigService dbTransferConfigService;

    /**
     * 获取所有数据库扩容配置列表
     *
     * @param dbTransferConfig 筛选条件
     * @param env 环境变量
     * @return 数据库扩容配置列表
     */
    @GetMapping(value = "/dbTransferConfigs")
    public BaseModel<Pager<DbTransferConfigDTO>> dbTransferConfigs(DbTransferConfig dbTransferConfig, Pager<DbTransferConfigDTO> pager,
                                                                   @PathVariable String env) {
        return BaseModel.getInstance(dbTransferConfigService.findList(dbTransferConfig, pager));
    }

    /**
     * 保存数据库扩容配置
     *
     * @param dbTransferConfig 数据库扩容配置
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/dbTransferConfig")
    public BaseModel<String> save(@RequestBody DbTransferConfig dbTransferConfig, @PathVariable String env) {
        dbTransferConfigService.save(dbTransferConfig);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取数据库扩容配置详情
     *
     * @param id 数据库扩容配置id
     * @param env 环境变量
     * @return 检点信息
     */
    @GetMapping(value = "/dbTransferConfig/{id}")
    public BaseModel<DbTransferConfig> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbTransferConfigService.detail(id));
    }

    /**
     * 修改数据库扩容配置
     *
     * @param dbTransferConfig 数据库扩容配置
     * @param env 环境变量
     * @return 是否成功
     */
    @PutMapping(value = "/dbTransferConfig")
    public BaseModel<String> update(@RequestBody DbTransferConfig dbTransferConfig, @PathVariable String env) {
        dbTransferConfigService.update(dbTransferConfig);
        return BaseModel.getInstance("success");
    }

    /**
     * 删除数据库扩容配置
     *
     * @param id 数据库扩容配置id
     * @param env 环境变量
     * @return 是否成功
     */
    @DeleteMapping(value = "/dbTransferConfig/{id}")
    public BaseModel<String> delete(@PathVariable Long id, @PathVariable String env) {
        dbTransferConfigService.delete(id);
        return BaseModel.getInstance("success");
    }
}
