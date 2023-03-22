<script setup>
import { reactive, ref } from "@vue/reactivity";
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";
import { Search } from '@element-plus/icons-vue'
import { del, get, post, put } from "../../../../tool/http";
import CarNumKeyboard from "../../../../components/CarNumKeyboard.vue";
//路由
const route = useRouter()

//字典
const typeDict = reactive({
  1: '机动车',
  2: '二轮车'
})

//分页信息
const total = ref(0)

const form = reactive({
  name: null,
  pageNo: 1,
  pageSize: 10

})
//页面数据
const data = ref()
function getPage () {
  get('/carNum/front/page', form).then(res => {
    if (res.code == 200) {
      data.value = res.data.records
      total.value = res.data.total
      form.pageNo = res.data.current
      form.pageSize = res.data.size
    } else if (res.code == 401) {
      ElMessage.error(res.msg)
      route.push("/")
    } else {
      ElMessage.error(res.msg)
    }
  })
}
getPage()


//分页信息变化获取数据
function handleSizeChange () {
  getPage()
}
function handleCurrentChange () {
  getPage()
}


const addForm = reactive({
  type: null,
  num: null
})

const updateForm = reactive({
  id: null,
  type: null,
  num: null
})
const openForm = ref(false)
function add () {
  addForm.num = ''
  addForm.type = ''
  openForm.value = true
}
function saveCarNum () {
  if (!child.value.checkInputFinshed()) {
    return
  }
  datetime.value = new Date().getTime()
  post('/carNum/front/insert', addForm).then(res => {
    if (res.code == 200) {
      ElMessage.success("添加成功")
      getPage()
      openForm.value = false
    } else if (res.code == 401) {
      ElMessage.error(res.msg)
      route.push("/")
    } else {
      ElMessage.error(res.msg)
    }
  })
}
//删除

function remove (value) {
  del(`/carNum/front/delete/${value}`).then(res => {
    if (res.code == 401) {
      ElMessage.error(res.msg)
      route.push("/")
    } else {
      ElMessage.success("删除成功")
      getPage()
    }
  })
}

const child = ref()
const datetime = ref(new Date().getTime())
const updateDatetime = ref(new Date().getTime())

function getCarNum (data) {
  addForm.num = data
}


// 编辑
const openUpdateForm = ref(false)
function openUpdate (row) {
  updateForm.id = row.id
  updateForm.num = row.num
  updateForm.type = row.type
  datetime.value = new Date().getTime()
  openUpdateForm.value = true
}
function getUpdateCarNum (data) {
  updateForm.num = data
}

function updateCarNum () {
  if (!child.value.checkInputFinshed()) {
    return
  }
  put('/carNum/front/update', updateForm).then(res => {
    if (res.code == 200) {
      ElMessage.success("修改成功")
      getPage()
      openUpdateForm.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>
<template>
  <div class="all-c">
    <h1>我的车牌</h1>
    <el-divider />
    <div class="contain-c">
      <el-form :inline="true" :model="form" class="demo-form-inline">

        <el-form-item><el-button type="default" size="small" plain @click="add">新增</el-button></el-form-item>

        <el-form-item label="车牌号码">
          <el-input v-model="form.num" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item label="车位类型">
          <el-select v-model="form.type" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="机动车" value="1" />
            <el-option label="二轮车" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="default" size="small" plain @click="getPage">查询</el-button></el-form-item>
      </el-form>



      <el-table :data="data" :cell-style="{ textAlign: 'center' }" stripe class="data-table" height="400px"
        :header-cell-style="{ 'text-align': 'center' }">
        <el-table-column type="index" width="100" />
        <el-table-column label="车位类型" width="180">
          <template v-slot="scope">
            {{ typeDict[scope.row.type] }}
          </template>
        </el-table-column>
        <el-table-column prop="num" label="车牌号码" width="180" />
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-button type="primary" @click="remove(scope.row.id)">删除</el-button>
            <el-button type="primary" @click="openUpdate(scope.row)">修改</el-button>
          </template>

        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>


    <el-dialog v-model="openForm" title="添加车牌号" width="700">
      <el-form-item required>
        <el-select v-model="addForm.type" placeholder="请选择车牌类型" style="margin: 0 auto;width: 70%;">
          <el-option label="机动车" value="1" />
          <el-option label="二轮车" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item required>
        <!-- <el-input autocomplete="off" style="width: 225px;" v-model="addForm.num" /> -->
        <CarNumKeyboard @sendCarNum="getCarNum" :key="datetime" ref="child" style="width: 70%; " />
      </el-form-item>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="openForm = false">取消</el-button>
          <el-button type="primary" @click="saveCarNum">
            添加
          </el-button>
        </span>
      </template>
    </el-dialog>


    <el-dialog v-model="openUpdateForm" title="编辑车牌号" width="700">
      <el-form-item>
        <el-select v-model="updateForm.type" placeholder=" " style="margin: 0 auto;width: 70%;">
          <el-option label="机动车" :value="1" />
          <el-option label="二轮车" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <CarNumKeyboard @sendCarNum="getUpdateCarNum" :key="updateDatetime" ref="child" style="width: 70%; " />
      </el-form-item>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="openUpdateForm = false">取消</el-button>
          <el-button type="primary" @click="updateCarNum">
            添加
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>


<style scoped>
.contain-c {
  text-align: center;
  display: flex;
  justify-content: center;
  flex-direction: column;
}

.data-table {
  width: 640px;
  margin: 0 auto;
}

.query-item {
  width: 100px;
}
</style>