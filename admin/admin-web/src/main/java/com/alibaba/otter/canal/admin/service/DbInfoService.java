package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.DbInfo;
import com.alibaba.otter.canal.admin.model.Pager;

import java.util.List;

public interface DbInfoService {

    void save(DbInfo dbInfo);

    DbInfo detail(Long id);

    void update(DbInfo dbInfo);

    void delete(Long id);

    List<DbInfo> findAll(DbInfo dbInfo);

    Pager<DbInfo> findList(DbInfo dbInfo, Pager<DbInfo> pager);
}
