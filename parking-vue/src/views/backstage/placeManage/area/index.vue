<script setup>
import { reactive, ref } from "@vue/reactivity";
import { ElMessage } from 'element-plus'

import { Search } from '@element-plus/icons-vue'
import { get, post } from "../../../../tool/http";

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
  get('/area/page', form).then(res => {
    data.value = res.data.records
    total.value = res.data.total
    form.pageNo = res.data.current
    form.pageSize = res.data.size
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


const areaForm = reactive({
  name: null
})
const addForm = ref(false)
function add () {
  addForm.value = true
}
function saveArea () {
  post('/area/insert', areaForm).then(res => {
    if (res.code == 200) {
      ElMessage.success("添加成功")
      areaForm.name = null
      getPage()
      addForm.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>
<template>
  <div class="all-c">
    <h1>区域管理</h1>
    <el-divider />
    <div class="contain-c">
      <el-form :inline="true" :model="form" class="demo-form-inline">

        <el-form-item><el-button type="default" size="small" plain @click="add">新增</el-button></el-form-item>

        <el-form-item label="区域名称">
          <el-input v-model="form.name" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item><el-button type="default" size="small" plain @click="getPage">查询</el-button></el-form-item>
      </el-form>



      <el-table :data="data" :cell-style="{ textAlign: 'center' }" stripe class="data-table" height="400px"
        :header-cell-style="{ 'text-align': 'center' }">
        <el-table-column prop="id" label="区域编号" width="180" />
        <el-table-column prop="name" label="区域名称" width="180" />


      </el-table>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>


    <el-dialog v-model="addForm" title="停车" width="600">
      <el-form :model="areaForm">
        <el-form-item label="区域名称" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="areaForm.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addForm = false">取消</el-button>
          <el-button type="primary" @click="saveArea">
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
  width: 360px;
  margin: 0 auto;
}

.query-item {
  width: 100px;
}
</style>