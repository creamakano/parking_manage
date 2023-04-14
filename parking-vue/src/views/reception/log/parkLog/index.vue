<script setup>
import { reactive, ref } from "@vue/reactivity";
import { Search } from '@element-plus/icons-vue'
import { get } from "../../../../tool/http";
import { ElMessage } from "element-plus";
//字典
const areaMap = reactive({})
const typeDict = reactive({
  1: '机动车',
  2: '二轮车'
})
const isPrivateDict = reactive({
  1: '私有车位',
  0: '公共车位'
})
const statusDict = reactive({
  0: '空闲',
  1: '停放中'
})
//区域
const area = ref()
get('/area/list').then(res => {
  area.value = res.data
  area.value.forEach(element => {
    areaMap[element.id] = element.name
  });
})
const form = reactive({
  type: null,
  carNum: null,
  areaId: null,
  code: null,
  pageNo: 1,
  pageSize: 10
})
const total = ref(0)

//页面数据
const data = ref()
function getPage () {
  get('/parkingLog/front/page', form).then(res => {
    if (res.code == 200) {
      data.value = res.data.records
      total.value = res.data.total
      form.pageNo = res.data.current
      form.pageSize = res.data.size
    } else {
      ElMessage.error(res.msg)
    }
  })
}
getPage()
function handleCurrentChange () {
  getPage()
}
function handleSizeChange () {
  getPage()
}
</script>
<template>
  <h1>停车记录</h1>
  <el-divider />
  <div class="contain-c">
    <el-form :inline="true" :model="form" class="demo-form-inline">
      <el-form-item label="车位编号">
        <el-input v-model="form.code" clearable class="query-item" size="small" />
      </el-form-item>
      <el-form-item label="区域">
        <el-select v-model="form.areaId" clearable class="query-item" size="small" placeholder=" ">
          <el-option v-for="item in area" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="所停车辆">
        <el-input v-model="form.carNum" clearable class="query-item" size="small" />
      </el-form-item>
      <el-form-item label="车位类型">
        <el-select v-model="form.type" clearable class="query-item" size="small" placeholder=" ">
          <el-option label="机动车" value="1" />
          <el-option label="二轮车" value="2" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getPage" size="small" :icon="Search" circle plain />
      </el-form-item>
    </el-form>


    <div class="table-form">

      <el-table :data="data" :cell-style="{ textAlign: 'center' }" stripe class="data-table"
        :header-cell-style="{ 'text-align': 'center' }" height="480">
        <el-table-column prop="areaId" label="区域" width="180">
          <template v-slot="scope">
            {{ areaMap[scope.row.areaId] }}
          </template>
        </el-table-column>
        <el-table-column prop="code" label="车位编号" width="180" />
        <el-table-column prop="carNum" label="所停车辆" width="180" />
        <el-table-column prop="type" label="车位类型" width="180">
          <template v-slot="scope">
            {{ typeDict[scope.row.type] }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column prop="cost" label="消费金额" width="180" />
      </el-table>
    </div>

    <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
      style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
      layout="total, sizes, prev, pager, next ,jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
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
  width: 1260px;
  margin: 0 auto;
}

.query-item {
  width: 100px;
}
</style>