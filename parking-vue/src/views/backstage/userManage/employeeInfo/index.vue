<script setup>
import { ElMessage } from "element-plus";
import { reactive, ref } from "vue";
import { del, get, post, put } from "../../../../tool/http";

//字典
const sex = reactive({
  0: '男',
  1: '女'
})
const position = reactive({
  '0': '普通员工',
  '1': '管理员'
})

const userInfo = ref([])
const queryForm = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0,
})
function getPage () {
  get('/employee/page', queryForm).then(res => {
    userInfo.value = res.data.records
    queryForm.total = res.data.total
  })
}
getPage()



//编辑
const updateDialog = ref(false)

const updateForm = reactive({
  id: '',
  password: '',
  name: '',
  code: null,
  education: '',
  age: '',
  position: '',
  phone: '',
  sex: '',
  address: '',
  birth: '',

})
function updateUser (row) {
  updateForm.id = row.id
  updateForm.password = row.password
  updateForm.code = row.code
  updateForm.name = row.name
  updateForm.phone = row.phone
  updateForm.sex = row.sex
  updateForm.address = row.address
  updateForm.birth = row.birth
  updateForm.education = row.education
  updateForm.age = row.age
  updateForm.position = row.position
  updateDialog.value = true
}
function handleClose () {
  updateDialog.value = false
}
function updateUserAction () {
  put('/employee/update', updateForm).then(res => {
    ElMessage.success('修改成功')
    getPage()
    updateDialog.value = false
  })
}

function deleteUser (id) {
  del(`/employee/delete/${id}`).then(res => {
    ElMessage.success('删除成功')
    getPage()
  })
}


//编辑
const insertDialog = ref(false)

const insertForm = reactive({
  name: '',
  code: null,
  education: '',
  age: '',
  position: '',
  phone: '',
  sex: '',
  address: '',
  birth: '',

})

function insertEmployee () {
  post('/employee/insert', insertForm).then(res => {
    if (res.code == 200) {
      ElMessage.success("新增成功")
      insertDialog.value = false
      getPage()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>
<template>
  <h1>员工信息管理</h1>
  <el-divider />
  <div class="query-line">
    <el-button type="primary" @click="insertDialog = true">新增员工</el-button>
  </div>

  <el-table :data="userInfo" stripe style="width: 70%;margin: 10px auto; " :cell-style="{ textAlign: 'center' }"
    height="570" :header-cell-style="{ 'text-align': 'center' }">
    <el-table-column prop="id" label="员工ID" />
    <el-table-column prop="code" label="员工编号" />
    <el-table-column prop="name" label="员工姓名" />
    <el-table-column prop="phone" label="手机号码" />
    <el-table-column prop="sex" label="员工性别">
      <template v-slot="scope">
        {{ sex[scope.row.sex] }}
      </template>
    </el-table-column>
    <el-table-column prop="birth" label="员工生日" />
    <el-table-column prop="education" label="员工学历" />
    <el-table-column prop="age" label="入职年龄" />
    <el-table-column prop="position" label="员工权限">
      <template v-slot="scope">
        {{ position[scope.row.position] }}
      </template>
    </el-table-column>
    <el-table-column label="操作">
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
      <el-form-item label="员工编号">
        <el-input v-model="updateForm.code" />
      </el-form-item>
      <el-form-item label="登录密码">
        <el-input v-model="updateForm.password" />
      </el-form-item>
      <el-form-item label="员工姓名">
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
      <el-form-item label="教育程度">
        <el-input v-model="updateForm.education" />
      </el-form-item>
      <el-form-item label="入职年龄">
        <el-input v-model="updateForm.age" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="updateForm.address" />
      </el-form-item>
      <el-form-item label="权限">
        <el-select v-model="updateForm.position" class="m-2" placeholder="Select">
          <el-option label="普通员工" :value="'0'" />
          <el-option label="管理员" :value="'1'" />
        </el-select>
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

  <el-dialog v-model="insertDialog" title="新增用户信息" width="30%">
    <el-form :label-position="right" label-width="100px" :model="updateForm" style="max-width: 460px">
      <el-form-item label="员工编号">
        <el-input v-model="insertForm.code" />
      </el-form-item>
      <el-form-item label="员工姓名">
        <el-input v-model="insertForm.name" />
      </el-form-item>
      <el-form-item label="登录密码">
        <el-input v-model="insertForm.password" />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="insertForm.phone" />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="insertForm.sex" class="m-2" placeholder="请选择性别">
          <el-option label="男" :value="0" />
          <el-option label="女" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="生日">
        <el-date-picker v-model="insertForm.birth" type="date" placeholder=" " value-format="YYYY-MM-DD"
          format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item label="教育程度">
        <el-input v-model="insertForm.education" />
      </el-form-item>
      <el-form-item label="入职年龄">
        <el-input v-model="insertForm.age" />
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="insertForm.address" />
      </el-form-item>
      <el-form-item label="权限">
        <el-select v-model="insertForm.position" class="m-2" placeholder="请选择权限">
          <el-option label="普通员工" :value="'0'" />
          <el-option label="管理员" :value="'1'" />
        </el-select>
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="insertDialog = false">取消</el-button>
        <el-button type="primary" @click="insertEmployee">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.query-line {
  width: 70%;
  display: flex;
  justify-content: end;
  margin: 0 auto;
}
</style>