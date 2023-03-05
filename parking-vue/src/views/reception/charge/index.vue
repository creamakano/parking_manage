<script setup>
import { reactive, ref } from "@vue/reactivity";
import { get, post, del } from "../../../tool/http";
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";
const route = useRouter()
//字典
const areaMap = reactive({})
const typeDict = reactive({
  1: '机动车',
  2: '二轮车'
})
const statusDict = reactive({
  0: '空闲',
  1: '充电中'
})
//车牌
const numList = ref()
function getNum (type) {
  get("/carNum/front/list", {
    type: type
  }).then(res => {
    if (res.code == 200) {
      numList.value = res.data
      openParkForm.value = true

    } else if (res.code == 401) {
      ElMessage.error(res.msg)
      route.push("/")
    } else {
      ElMessage.error(res.msg)
    }
  })
}


const form = reactive({
  type: null,

  code: null,

  pageNo: 1,
  pageSize: 10,

})
const total = ref(0)
//页面数据
const data = ref()
function getPage () {
  get('/charge/front/page', form).then(res => {
    data.value = res.data.records
    total.value = res.data.total
    form.pageNo = res.data.current
    form.pageSize = res.data.size
  })
}
getPage()

//分页信息
function handleSizeChange () {
  getPage()
}
function handleCurrentChange () {
  getPage()
}

//充电，断电
const openParkForm = ref(false)
const parkForm = ref({})
function start (row) {
  parkForm.value = row
  getNum(parkForm.value.type)
  openParkForm.value = true

}
function parkAction () {
  // console.log(parkForm);
  post('/charge/start', parkForm.value).then(res => {
    if (res.code == 200) {
      ElMessage.success("停车成功")
      getPage()
      openParkForm.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

function stop (id) {
  post(`/charge/stop/${id}`).then(res => {
    if (res.code == 200) {
      document.querySelector('body').innerHTML = res.data
      document.forms[0].submit()
    } else {
      ElMessage.error(res.msg)

    }
  })
}
</script>
<template>
  <div class="all-c">
    <h1>车位信息管理</h1>
    <el-divider />
    <div class="contain-c">

      <el-form :inline="true" :model="form" class="demo-form-inline">

        <el-form-item label="车位类型">
          <el-select v-model="form.type" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="机动车" value="1" />
            <el-option label="二轮车" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="车位编号">
          <el-input v-model="form.code" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item label="所停车辆">
          <el-input v-model="form.carNum" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item><el-button type="default" size="small" plain @click="getPage">查询</el-button></el-form-item>

      </el-form>


      <div class="table-form">
        <el-table :data="data" stripe height="400" :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center' }">

          <el-table-column prop="code" label="车位编号" width="180" />
          <el-table-column label="车位类型" width="180">
            <template v-slot="scope">
              {{ typeDict[scope.row.type] }}
            </template>
          </el-table-column>
          <el-table-column label="车位状态" width="180">
            <template v-slot="scope">
              {{ statusDict[scope.row.status] }}
            </template>
          </el-table-column>

          <el-table-column prop="carNum" label="所停车辆" width="180" />
          <el-table-column label="操作" width="180">
            <template v-slot="scope">
              <el-button v-if="scope.row.status == 1" type="danger" @click="stop(scope.row.id)">断电</el-button>
              <el-button v-else type="primary" @click="start(scope.row)">充电</el-button>
            </template>

          </el-table-column>
        </el-table>
      </div>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next ,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>





    <el-dialog v-model="openParkForm" title="充电" width="600">
      <el-form :model="parkForm">
        <el-form-item label="车位编号" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="parkForm.code" disabled />
        </el-form-item>
        <el-form-item label="车位类型" label-width="180">
          <el-input autocomplete="off" style="width: 225px;" v-model="typeDict[parkForm.type]" disabled />
        </el-form-item>
        <el-form-item label="所停车辆" label-width="180">
          <el-select v-model="parkForm.num" placeholder=" ">
            <el-option v-for="item in numList" :key="item.id" :label="item.num" :value="item.num"
              style="width: 225px;" />
          </el-select>
        </el-form-item>
        <el-form-item label="充电时间" label-width="180">
          <el-time-select v-model="parkForm.chargeTime" start="00:15" step="00:15" end="12:00" placeholder=" "
            style="width: 225px;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="openParkForm = false">取消</el-button>
          <el-button type="primary" @click="parkAction">
            开始充电
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
  align-items: center;
  flex-direction: column;
}

.data-table {
  width: 1080px;
  margin: 0 auto;
}

.query-item {
  width: 100px;
}

.pagination {
  text-align: center;
}
</style>