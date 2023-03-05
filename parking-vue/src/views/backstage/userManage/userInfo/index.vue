<script setup>
import { ElMessage } from "element-plus";
import { reactive, ref } from "vue";
import { del, get, put } from "../../../../tool/http";

//字典
const sex = reactive({
  0: '男',
  1: '女'
})

const userInfo = ref([])
const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0,
})
function getPage () {
  get('/user/page', queryForm).then(res => {
    userInfo.value = res.data.records
    queryForm.total = res.data.total
  })
}
getPage()



//编辑
const updateDialog = ref(false)

const updateForm = reactive({
  id: '',
  name: '',
  phone: '',
  sex: '',
  address: '',
  birth: '',

})
function updateUser (row) {
  updateForm.id = row.id
  updateForm.name = row.name
  updateForm.phone = row.phone
  updateForm.sex = row.sex
  updateForm.address = row.address
  updateForm.birth = row.birth
  updateDialog.value = true
}
function handleClose () {
  updateDialog.value = false
}
function updateUserAction () {
  put('/user/update', updateForm).then(res => {
    ElMessage.success('修改成功')
    getPage()
    updateDialog.value = false
  })
}

function deleteUser (id) {
  del(`/user/delete/${id}`).then(res => {
    ElMessage.success('删除成功')
    getPage()
  })
}
</script>
<template>
  <h1>用户信息管理</h1>
  <el-divider />
  <el-table :data="userInfo" stripe style="width: 50%;margin: 60px auto; " :cell-style="{ textAlign: 'center' }"
    height="570" :header-cell-style="{ 'text-align': 'center' }">
    <el-table-column prop="id" label="用户ID" />
    <el-table-column prop="name" label="用户名称" />
    <el-table-column prop="phone" label="手机号码" />
    <el-table-column prop="sex" label="用户性别">
      <template v-slot="scope">
        {{ sex[scope.row.sex] }}
      </template>
    </el-table-column>
    <el-table-column prop="address" label="用户地址" />
    <el-table-column prop="birth" label="用户生日" />
    <el-table-column prop="birth" label="操作">
      <template v-slot="scope">
        <el-button type="warning" @click="updateUser(scope.row)" size="small">修改</el-button>
        <el-button type="danger" @click="deleteUser(scope.row.id)" size="small">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination v-model:current-page="queryForm.pageNo" v-model:page-size="queryForm.pageSize" :total="queryForm.total"
    style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
    layout="total, sizes, prev, pager, next ,jumper" @size-change="handleSizeChange"
    @current-change="handleCurrentChange" />


  <el-dialog v-model="updateDialog" title="编辑用户信息" width="30%" :before-close="handleClose">
    <el-form :label-position="right" label-width="100px" :model="updateForm" style="max-width: 460px">
      <el-form-item label="用户姓名">
        <el-input v-model="updateForm.name" />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="updateForm.phone" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="updateForm.sex" class="m-2" placeholder="Select">
          <el-option label="男" :value="0" />
          <el-option label="女" :value="1" />

        </el-select>
      </el-form-item>
      <el-form-item label="生日">
        <el-date-picker v-model="updateForm.birth" type="date" placeholder=" " value-format="YYYY-MM-DD"
          format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="updateForm.address" />
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="updateUserAction">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>