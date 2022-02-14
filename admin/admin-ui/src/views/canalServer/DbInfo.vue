<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.dbIp" placeholder="数据库IP" style="width: 200px;" class="filter-item" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建数据库</el-button>
      <el-button class="filter-item" type="info" @click="fetchData()">刷新列表</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="数据库名" min-width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.dbName }}
        </template>
      </el-table-column>
      <el-table-column label="数据库IP" min-width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.dbIp }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据库端口" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.dbPort}}
        </template>
      </el-table-column>
      <el-table-column label="顺序" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.sequence}}
        </template>
      </el-table-column>
      <el-table-column class-db-name="status-col" label="状态" min-width="150" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusLabel }}</el-tag>
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
        <el-form-item label="数据库IP" prop="dbIp">
          <el-input v-model="nodeModel.dbIp" />
        </el-form-item>
        <el-form-item label="数据库端口" prop="dbPort">
          <el-input v-model="nodeModel.dbPort" placeholder="3306" type="number" />
        </el-form-item>
        <el-form-item label="数据库名" prop="dbName">
          <el-input v-model="nodeModel.dbName" />
        </el-form-item>
        <el-form-item label="数据库用户" prop="dbUserName">
          <el-input v-model="nodeModel.dbUserName" />
        </el-form-item>
        <el-form-item label="数据库密码" prop="dbPassword">
          <el-input v-model="nodeModel.dbPassword" />
        </el-form-item>
        <el-form-item label="顺序" prop="sequence">
          <el-input v-model="nodeModel.sequence" placeholder="升序排序" type="number" />
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
import { addDbInfo, getDbInfos, updateDbInfo, deleteDbInfo } from '@/api/dbInfo'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'gray'
      }
      return statusMap[status]
    },
    statusLabel(status) {
      const statusMap = {
        1: '连接成功',
        0: '连接失败'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      instanceList: null,
      listLoading: true,
      count: 0,
      listQuery: {
        dbName: '',
        dbIp: '',
        page: 1,
        size: 20
      },
      dialogFormVisible: false,
      textMap: {
        create: '新建数据库信息',
        update: '修改数据库信息'
      },
      nodeModel: {
        id: undefined,
        dbName: null,
        dbIp: null,
        dbPort: 3306,
        dbUserName: null,
        dbPassword: null,
        sequence: 0,
        status: 0
      },
      rules: {
        dbName: [{ required: true, message: '数据库名不能为空', trigger: 'change' }],
        dbIp: [{ required: true, message: '数据库IP不能为空', trigger: 'change' }],
        dbPort: [{ required: true, message: '数据库端口不能为空', trigger: 'change' }],
        dbUserName: [{ required: true, message: '数据库用户不能为空', trigger: 'change' }],
        dbPassword: [{ required: true, message: '数据库密码不能为空', trigger: 'change' }],
        sequence: [{ required: true, message: '顺序不能为空', trigger: 'change' }]
      },
      dialogStatus: 'create'
    }
  },
  // { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'change' }
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getDbInfos(this.listQuery).then(res => {
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
        dbName: null,
        dbIp: null,
        dbPort: 3306,
        dbUserName: null,
        dbPassword: null,
        sequence: 0,
        status: 0
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
            addDbInfo(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateDbInfo(this.nodeModel).then(res => {
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
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除数据库会导致扩容信息丢失', '确定删除数据库信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDbInfo(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除数据库信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除数据库信息失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
