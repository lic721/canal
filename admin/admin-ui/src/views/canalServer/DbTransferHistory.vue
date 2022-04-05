<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="迁移源数据库" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.sourceDbName+' '+scope.row.sourceDbIp+':'+scope.row.sourceDbPort }}
        </template>
      </el-table-column>
      <el-table-column label="迁移目标数据库" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.targetDbName+' '+scope.row.targetDbIp+':'+scope.row.targetDbPort }}
        </template>
      </el-table-column>
      <el-table-column label="扩容开始时间" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}
        </template>
      </el-table-column>
      <el-table-column label="扩容完成时间" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <el-table-column label="mq的topic" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.mqTopic }}
        </template>
      </el-table-column>
      <el-table-column label="Instance名称" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.canalInstanceName }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" min-width="150">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleHistoryTable(scope.row)">触发扩容的表</el-dropdown-item>
              <el-dropdown-item @click.native="handleHistoryTenant(scope.row)">触发扩容的租户</el-dropdown-item>
              <el-dropdown-item @click.native="handleHistoryTenant(scope.row)">标记为扩容完成</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="fetchData()" />

    <el-dialog :visible.sync="historyTableFormVisible" title="触发扩容的表" width="600px">
      <el-form label-position="left" label-width="120px" style="width: 400px; margin-left:30px;">
        <el-form-item v-for="(item) in historyTablesForm.historyTables" label="表名" :key="item.index">
          <el-input v-model="item.tableName" style="width:100%" readonly />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyTableFormVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="historyTenantFormVisible" title="触发扩容的租户" width="600px">
      <el-form label-position="left" label-width="120px" style="width: 400px; margin-left:30px;">
        <el-form-item v-for="(item) in historyTenantsForm.historyTenants" label="租户编码" :key="item.index">
          <el-input v-model="item.tenantCode" style="width:100%" readonly />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyTenantFormVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDbTransferHistoryList } from '@/api/dbTransferHistory'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  data() {
    return {
      list: null,
      instanceList: null,
      listLoading: true,
      count: 0,
      listQuery: {
        page: 1,
        size: 20
      },
      historyTableFormVisible: false,
      historyTablesForm: {
        id: undefined, // db_transfer_history的id
        historyTables: []
      },
      historyTenantFormVisible: false,
      historyTenantsForm: {
        id: undefined, // db_transfer_history的id
        historyTenants: []
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getDbTransferHistoryList(this.listQuery).then(res => {
        this.list = res.data.items
        this.count = res.data.count
      }).finally(() => {
        this.listLoading = false
      })
    },
    queryData() {
      this.listQuery.page = 1
      this.fetchData()
    },
    handleHistoryTable(row) {
      this.historyTablesForm.id = row.id
      this.historyTablesForm.historyTables = row.historyTables
      this.historyTableFormVisible = true
    },
    handleHistoryTenant(row) {
      this.historyTenantsForm.id = row.id
      this.historyTenantsForm.historyTenants = row.historyTenants
      this.historyTenantFormVisible = true
    }
  }
}
</script>
