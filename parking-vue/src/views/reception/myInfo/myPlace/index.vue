<script setup>
import { reactive, ref } from "@vue/reactivity";
import { Search, Plus } from '@element-plus/icons-vue'
import { get, post, del } from "../../../../tool/http";
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router";
import CarNumKeyboard from "../../../../components/CarNumKeyboard.vue";

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
  carNum: null

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

//车位租借
const rentForm = reactive({
  id: '',
  carNum: ''
})
const rentDialog = ref(false)
function openRentDialog (row) {
  datetime.value = new Date().getTime()
  rentForm.id = row.id
  rentDialog.value = true
}
const child = ref()
const datetime = ref(new Date().getTime())
function getCarNum (data) {
  rentForm.carNum = data
}

function rent () {
  if (!child.value.checkInputFinshed()) {
    return
  }
  post('/carNumPlaceRel/rent', rentForm).then(res => {
    if (res.code == 200) {
      ElMessage.success('租借成功')
      getPage()
      rentDialog.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 收回车位
function getBack(id){
  post('/carNumPlaceRel/getBack', {id:id}).then(res => {
    if (res.code == 200) {
      ElMessage.success('收回成功')
      getPage()
      rentDialog.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
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
          <el-table-column prop="carNum" label="现所属车牌" width="180">
          </el-table-column>
          <el-table-column prop="originalCarNum" label="原所属车牌" width="180">
          </el-table-column>
          <el-table-column label="租借状态" width="180">
            <template v-slot="scope">
              <template v-if="scope.row.isRent == 0">未租借</template>
              <template v-else>租借中</template>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template v-slot="scope">
              <template v-if="scope.row.isRent == 1">
                <el-button type="warning" size="small" @click="getBack(scope.row.id)">收回</el-button>
              </template>
              <el-button type="warning" size="small" @click="openRentDialog(scope.row)">租借</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next ,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
  </div>


  <el-dialog v-model="rentDialog" title="租借" width="600">
    <el-form :model="parkForm" label-position="top" style="padding: 0 40px;">
      <el-form-item label="租借车牌号" label-width="180" style="margin: 0 auto">
        <CarNumKeyboard @sendCarNum="getCarNum" :key="datetime" ref="child" style="width:520px;margin: 0 0;" />
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="rentDialog = false">取消</el-button>
        <el-button type="primary" @click="rent">
          租借
        </el-button>
      </span>
    </template>
  </el-dialog>
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