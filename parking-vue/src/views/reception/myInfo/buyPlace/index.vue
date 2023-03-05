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
//车牌
const numList = ref()
function getNum (type) {
  get("/carNum/front/listWithoutBuy", {
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
  areaId: null,
  code: null,
  isPrivate: 0,
  pageNo: 1,
  pageSize: 10,
  status: 0

})
const total = ref(0)
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

//购买车位dialog
const parkForm = ref()
const openParkForm = ref(false)
function toOpenParkForm (row) {
  parkForm.value = row
  getNum(parkForm.value.type)

}

function buy () {
  console.log(parkForm.value);
  post('/parking/buy',parkForm.value).then(res => {
    if (res.code == 200) {
      document.querySelector('body').innerHTML = res.data
      document.forms[0].submit()
    }
    else if (res.code == 401) {
      ElMessage.error(res.msg)
      route.push("/")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

function cancel () {
  parkForm.value = ''
  openParkForm.value = false
}





function openUpdate (value) {
  console.log(value);
}

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
    <h1>购买车位</h1>
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
        <el-form-item label="区域">
          <el-select v-model="form.areaId" clearable class="query-item" size="small" placeholder=" ">
            <el-option v-for="item in area" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item><el-button type="default" size="small" plain @click="queryPage">查询</el-button></el-form-item>

      </el-form>


      <div class="table-form">
        <el-table :data="data" stripe height="530" :header-cell-style="{ 'text-align': 'center' }"
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
          <el-table-column prop="isPrivate" label="车位信息" width="180">
            <template v-slot="scope">
              {{ isPrivateDict[scope.row.isPrivate] }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template v-slot="scope">
              <el-button type="primary" @click="toOpenParkForm(scope.row)">购买</el-button>
            </template>

          </el-table-column>
        </el-table>
      </div>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next ,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>




    <el-dialog v-model="openParkForm" title="购买" width="600">
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
          <el-select v-model="parkForm.num" placeholder=" ">
            <el-option v-for="item in numList" :key="item.id" :label="item.num" :value="item.num" style="width: 225px;" />
          </el-select>

        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="buy">
            购买  
          </el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 
        <el-dialog v-model="openAdd" title="新增车位" width="600">
          <el-form :model="addForm">
            <el-form-item label="区域" label-width="180" required>
              <el-select v-model="addForm.areaId" placeholder=" ">
                <el-option v-for="item in area" :key="item.id" :label="item.name" :value="item.id" style="width: 225px;" />
              </el-select>
            </el-form-item>
            <el-form-item label="车位编号" label-width="180" required>
              <el-input v-model="addForm.code" autocomplete="off" style="width: 225px;" />
            </el-form-item>
            <el-form-item label="车位类型" label-width="180" required>
              <el-select v-model="addForm.type" placeholder=" " style="width: 225px;">
                <el-option label="机动车" value="1" />
                <el-option label="二轮车" value="2" />
              </el-select>
            </el-form-item>
            <el-form-item label="是否私人" label-width="180" required>
              <el-select v-model="addForm.isPrivate" placeholder=" " style="width: 225px;">
                <el-option label="否" value="0" />
                <el-option label="是" value="1" />
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="openAdd = false">取消</el-button>
              <el-button type="primary" @click="submitAdd">
                确定
              </el-button>
            </span>
          </template>
        </el-dialog> -->
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