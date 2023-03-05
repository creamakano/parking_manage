<script setup>
import { reactive, ref } from "@vue/reactivity";
import { Search, Plus } from '@element-plus/icons-vue'
import { get, post, del } from "../../../../tool/http";
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";
const route = useRouter()
//字典
const areaMap = reactive({})
const typeDict = reactive({
  1: '机动车',
  2: '二轮车'
})
const isPrivateDict = reactive({
  1: '私人车位',
  0: '公共车位'
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
  areaId: null,
  code: null,
  pageNo: 1,
  pageSize: 10,
  carNum:null

})
const total = ref(0)
//页面数据
const data = ref('')
function getPage () {
  get('/parking/front/myPlace', form).then(res => {
    data.value = res.data.records
    total.value = res.data.total
    form.pageNo = res.data.current
    form.pageSize = res.data.size
  })
}
getPage()

function queryPage () {
  getPage()
}

//分页信息
function handleSizeChange () {
  getPage()
}
function handleCurrentChange () {
  getPage()
}
</script>
<template>
  <div class="all-c">
    <h1>我的车位</h1>
    <el-divider />
    <div class="contain-c">

      <el-form :inline="true" :model="form" class="demo-form-inline">

        <el-form-item label="车位类型">
          <el-select v-model="form.type" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="机动车" value="1" />
            <el-option label="二轮车" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属车牌">
          <el-input v-model="form.carNum" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item label="车位编号">
          <el-input v-model="form.code" clearable class="query-item" size="small" />
        </el-form-item>
        <el-form-item label="区域">
          <el-select v-model="form.areaId" clearable class="query-item" size="small" placeholder=" ">
            <el-option v-for="item in area" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item><el-button type="default" size="small" plain @click="queryPage">查询</el-button></el-form-item>

      </el-form>


      <div class="table-form">
        <el-table :data="data" stripe height="400" :header-cell-style="{ 'text-align': 'center' }"
          :cell-style="{ 'text-align': 'center' }">
          <el-table-column label="区域" width="180">
            <template v-slot="scope">
              {{ areaMap[scope.row.areaId] }}
            </template>
          </el-table-column>
          <el-table-column prop="code" label="车位编号" width="180" />
          <el-table-column label="车位类型" width="180">
            <template v-slot="scope">
              {{ typeDict[scope.row.type] }}
            </template>
          </el-table-column>
          <el-table-column prop="carNum" label="所属车牌" width="180">
          </el-table-column>
        </el-table>
      </div>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next ,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
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
  width: 900px;
  margin: 0 auto;
}

.query-item {
  width: 100px;
}

.pagination {
  text-align: center;
}
</style>