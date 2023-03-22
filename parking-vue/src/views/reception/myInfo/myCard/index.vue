<script setup>
import { reactive, ref } from "@vue/reactivity";
import { ElMessage } from 'element-plus'

import { get, post } from "../../../../tool/http";

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
  get('/card/page', form).then(res => {
    data.value = res.data.records
    total.value = res.data.total
    form.pageNo = res.data.current
    form.pageSize = res.data.size
  })
}
getPage()


//分页信息变化获取数据
function handleSizeChange (val) {
  form.pageSize = val
  getPage()
}
function handleCurrentChange (val) {
  form.pageNo = val
  getPage()
}



</script>
<template>
  <div class="all-c">
    <h1>我的套餐</h1>
    <el-divider />
    <div class="contain-c">
      <el-form :inline="true" :model="form" class="demo-form-inline">
        <el-form-item label="车牌号">
          <el-input v-model="form.carNum" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item label="车位类型">
          <el-select v-model="form.carType" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="机动车" value="1" />
            <el-option label="二轮车" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="default" size="small" plain @click="getPage">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="data" :cell-style="{ textAlign: 'center' }" stripe class="data-table" height="400px"
        :header-cell-style="{ 'text-align': 'center' }">
        <el-table-column type="index" label="序号" width="100" />
        <el-table-column prop="carNum" label="车牌号码" width="180" />
        <el-table-column prop="carType" label="车位类型" width="180">
          <template v-slot="scope">
            {{ typeDict[scope.row.carType] }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="优惠结束时间" width="180" />


      </el-table>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>

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