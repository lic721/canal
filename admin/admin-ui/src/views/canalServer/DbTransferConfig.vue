<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建扩容配置</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="数据库名" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.dbName }}
        </template>
      </el-table-column>
      <el-table-column label="数据库IP" min-width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.dbIp }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据库端口" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.dbPort }}
        </template>
      </el-table-column>
      <el-table-column label="租户编码的数据库字段名称" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.tenantCodeColumnName }}
        </template>
      </el-table-column>
      <el-table-column label="用于触发扩容的关键表名" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.keyTableName }}
        </template>
      </el-table-column>
      <el-table-column label="表数据量阈值" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.tableCountThreshold }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" min-width="150">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="fetchData()" />
    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="left" label-width="120px" style="width: 400px; margin-left:30px;">
        <el-form-item label="数据库实例" prop="dbInfoId">
          <el-select v-model="nodeModel.dbInfoId" placeholder="请选择" style="width:100%">
            <el-option
              v-for="item in dbInfos"
              :key="item.id"
              :label="item.dbName+' '+item.dbIp+':'+item.dbPort"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="租户编码的数据库字段名称" prop="tenantCodeColumnName">
          <el-input v-model="nodeModel.tenantCodeColumnName" placeholder="tenant_code" />
        </el-form-item>
        <el-form-item label="用于触发扩容的关键表名" prop="keyTableName">
          <el-input v-model="nodeModel.keyTableName" placeholder="order_info等关键表"/>
        </el-form-item>
        <el-form-item label="表数据量阈值" prop="tableCountThreshold">
          <el-input v-model="nodeModel.tableCountThreshold" type="number" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataOperation()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addDbTransferConfig, getDbTransferConfigs, updateDbTransferConfig, deleteDbTransferConfig } from '@/api/dbTransferConfig'
import { getAllDbInfos } from '@/api/dbInfo'
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
      dialogFormVisible: false,
      textMap: {
        create: '新建扩容配置',
        update: '修改扩容配置'
      },
      dbInfos: [],
      nodeModel: {
        id: undefined,
        tenantCodeColumnName: '',
        keyTableName: '',
        tableCountThreshold: 100000,
        dbInfoId: undefined
      },
      rules: {
        tenantCodeColumnName: [{ required: true, message: '租户编码的数据库字段名称不能为空', trigger: 'change' }],
        keyTableName: [{ required: true, message: '用于触发扩容的关键表名不能为空', trigger: 'change' }],
        tableCountThreshold: [{ required: true, message: '表数据量阈值不能为空', trigger: 'change' }],
        dbInfoId: [{ required: true, message: '数据库实例不能为空', trigger: 'change' }]
      },
      dialogStatus: 'create'
    }
  },
  // { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'change' }
  created() {
    getAllDbInfos().then(res => {
      this.dbInfos = res.data
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getDbTransferConfigs(this.listQuery).then(res => {
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
    resetModel() {
      this.nodeModel = {
        id: undefined,
        tenantCodeColumnName: '',
        keyTableName: '',
        tableCountThreshold: 100000,
        dbInfoId: undefined
      }
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addDbTransferConfig(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateDbTransferConfig(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    operationRes(res) {
      if (res.data === 'success') {
        this.fetchData()
        this.dialogFormVisible = false
        this.$message({
          message: this.textMap[this.dialogStatus] + '成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: this.textMap[this.dialogStatus] + '失败',
          type: 'error'
        })
      }
    },
    handleUpdate(row) {
      this.resetModel()
      this.nodeModel = Object.assign({}, row)
      console.log(this.dbInfos)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除配置会导致扩容信息丢失', '确定删除配置信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDbTransferConfig(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除配置成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除配置失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
