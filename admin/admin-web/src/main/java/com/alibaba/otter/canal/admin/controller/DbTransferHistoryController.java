package com.alibaba.otter.canal.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.otter.canal.admin.dto.DbTransferHistoryDTO;
import com.alibaba.otter.canal.admin.model.BaseModel;
import com.alibaba.otter.canal.admin.model.DbTransferHistory;
import com.alibaba.otter.canal.admin.model.Pager;
import com.alibaba.otter.canal.admin.service.DbTransferHistoryService;

/**
 * 扩容历史
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/{env}")
public class DbTransferHistoryController {

    @Autowired
    DbTransferHistoryService dbTransferHistoryService;

    /**
     * 获取所有数据库扩容历史列表
     *
     * @param dbTransferHistory 筛选条件
     * @param env 环境变量
     * @return 数据库扩容历史列表
     */
    @GetMapping(value = "/dbTransferHistoryList")
    public BaseModel<Pager<DbTransferHistoryDTO>> dbTransferHistoryList(DbTransferHistory dbTransferHistory, Pager<DbTransferHistoryDTO> pager,
                                                                   @PathVariable String env) {
        return BaseModel.getInstance(dbTransferHistoryService.findList(dbTransferHistory, pager));
    }

    /**
     * 保存数据库扩容历史
     *
     * @param dbTransferHistory 数据库扩容历史
     * @param env 环境变量
     * @return 是否成功
     */
    @PostMapping(value = "/dbTransferHistory")
    public BaseModel<String> save(@RequestBody DbTransferHistory dbTransferHistory, @PathVariable String env) {
        dbTransferHistoryService.save(dbTransferHistory);
        return BaseModel.getInstance("success");
    }

    /**
     * 获取数据库扩容历史详情
     *
     * @param id 数据库扩容历史id
     * @param env 环境变量
     * @return 检点信息
     */
    @GetMapping(value = "/dbTransferHistory/{id}")
    public BaseModel<DbTransferHistory> detail(@PathVariable Long id, @PathVariable String env) {
        return BaseModel.getInstance(dbTransferHistoryService.detail(id));
    }

}
