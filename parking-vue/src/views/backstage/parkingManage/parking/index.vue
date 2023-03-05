<script setup>
import { reactive, ref } from "@vue/reactivity";
import { ElMessage } from 'element-plus'

import { Search } from '@element-plus/icons-vue'
import { get, post } from "../../../../tool/http";
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

//分页信息
const total = ref(0)



const form = reactive({
  type: null,
  status: null,
  carNum: null,
  area: null,
  pageNo: 1,
  pageSize: 10,
  isPrivate: 0

})
//页面数据
const data = ref()
function getPage () {
  get('/parking/page', form).then(res => {
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

//停车、取车
const openParkForm = ref(false)
const parkForm = ref({})
function park (row) {
  parkForm.value = row
  console.log(parkForm.areaId);

  openParkForm.value = true

}
function parkAction () {
  console.log(parkForm.value);
  post('/parking/park', parkForm.value).then(res => {
    if (res.code == 200) {
      ElMessage.success(res.msg)
      getPage()
      openParkForm.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

function pickUp (id) {
  post(`/parking/pickUp/${id}`, {
    returnUrl: '/back/home/parking'
  }).then(res => {
    if (res.code == 200) {
      document.querySelector('body').innerHTML = res.data
      document.forms[0].submit()
    }else if (res.code == 201) {
      ElMessage.success(res.msg)
      getPage()
    }else {
      ElMessage.error(res.msg)
    }
  })
}
</script>
<template>
  <div class="all-c">
    <h1>停车管理</h1>
    <el-divider />
    <div class="contain-c">
      <el-form :inline="true" :model="form" class="demo-form-inline">
        <el-form-item label="车位类型">
          <el-select v-model="form.type" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="机动车" value="1" />
            <el-option label="二轮车" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="车位状态">
          <el-select v-model="form.status" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="空闲" value="0" />
            <el-option label="停放中" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="所停车辆">
          <el-input v-model="form.carNum" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item label="区域">
          <el-select v-model="form.areaId" clearable class="query-item" size="small" placeholder=" ">
            <el-option v-for="item in area" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getPage" size="small" :icon="Search" circle plain />
        </el-form-item>
      </el-form>



      <el-table :data="data" :cell-style="{ textAlign: 'center' }" stripe class="data-table" height="540"
        :header-cell-style="{ 'text-align': 'center' }">
        <el-table-column prop="areaId" label="区域" width="180">
          <template v-slot="scope">
            {{ areaMap[scope.row.areaId] }}
          </template>
        </el-table-column>
        <el-table-column prop="code" label="车位编号" width="180" />
        <el-table-column prop="status" label="车位状态" width="180">
          <template v-slot="scope">
            {{ statusDict[scope.row.status] }}
          </template>
        </el-table-column>
        <el-table-column prop="type" label="车位类型" width="180">
          <template v-slot="scope">
            {{ typeDict[scope.row.type] }}
          </template>
        </el-table-column>
        <el-table-column prop="carNum" label="所停车辆" width="180" />
        <el-table-column label="操作" width="180">
          <template v-slot="scope">
            <el-button v-if="scope.row.status == 1" type="danger" @click="pickUp(scope.row.id)">取车</el-button>
            <el-button v-else type="primary" @click="park(scope.row)">停车</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>


    <el-dialog v-model="openParkForm" title="停车" width="600">
      <el-form :model="parkForm">
        <el-form-item label="区域" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="areaMap[parkForm.areaId]" disabled />
        </el-form-item>
        <el-form-item label="车位编号" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="parkForm.code" disabled />
        </el-form-item>
        <el-form-item label="车位类型" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="typeDict[parkForm.type]" disabled />
        </el-form-item>
        <el-form-item label="所停车辆" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="parkForm.num" />
        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="openParkForm = false">取消</el-button>
          <el-button type="primary" @click="parkAction">
            停车
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
  width: 1080px;
  margin: 0 auto;
}

.query-item {
  width: 100px;
}
</style>