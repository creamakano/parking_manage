<script setup>
import { reactive, ref } from "@vue/reactivity";
import { Search, Plus } from '@element-plus/icons-vue'
import { get, post, del } from "../../../../tool/http";
import { ElMessage } from 'element-plus'

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
  isPrivate: '',
  pageNo: 1,
  pageSize: 10,

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


function remove (value) {
  del(`/parking/delete/${value}`).then(res => {
    getPage()
  })
}

const updateForm = reactive({
  id: '',
  areaId: '',
  code: '',
  type: 0
})
const openUpdateModel = ref(false)
function openUpdate (row) {
  updateForm.id = row.id
  updateForm.areaId = row.areaId
  updateForm.code = row.code
  updateForm.type = Number(row.type)
  console.log(updateForm.type);
  openUpdateModel.value = true
}

function queryPage () {
  getPage()
}






//dialog
const openAdd = ref(false)
function add () {
  openAdd.value = true
}

const addForm = reactive({
  areaId: '',
  code: null,
  type: null,
  isPrivate: ''
})

function submitAdd () {
  console.log((addForm));
  post('/parking/insert', addForm).then((res) => {
    if (res.code == 200) {
      ElMessage.success('添加成功')
      getPage()
      openAdd.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

function submitUpdate () {
  post('/parking/update', updateForm).then((res) => {
    if (res.code == 200) {
      ElMessage.success('添加成功')
      getPage()
      openUpdateModel.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
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
    <h1>车位信息管理</h1>
    <el-divider />
    <div class="contain-c">

      <el-form :inline="true" :model="form" class="demo-form-inline">
        <el-form-item><el-button type="default" size="small" plain @click="add">新增</el-button></el-form-item>

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
        <el-form-item label="车位信息">
          <el-select v-model="form.isPrivate" clearable class="query-item" size="small" placeholder=" ">
            <el-option label="私人车位" value="1" />
            <el-option label="公共车位" value="0" />
          </el-select>
        </el-form-item>

        <el-form-item><el-button type="default" size="small" plain @click="queryPage">查询</el-button></el-form-item>

      </el-form>


      <div class="table-form">
        <el-table :data="data" stripe height="570" :header-cell-style="{ 'text-align': 'center' }"
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
              <el-button type="primary" @click="remove(scope.row.id)">删除</el-button>
              <el-button type="primary" @click="openUpdate(scope.row)">修改</el-button>
            </template>

          </el-table-column>
        </el-table>
      </div>
      <el-pagination v-model:current-page="form.pageNo" v-model:page-size="form.pageSize"
        style=" display: flex; justify-content: center;" :page-sizes="[10, 20, 50, 100]" class="pagination"
        layout="total, sizes, prev, pager, next ,jumper" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>


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
        <!-- <el-form-item label="是否私人" label-width="180" required>
                      <el-select v-model="addForm.isPrivate" placeholder=" " style="width: 225px;">
                        <el-option label="否" value="0" />
                        <el-option label="是" value="1" />
                      </el-select>
                    </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="openAdd = false">取消</el-button>
          <el-button type="primary" @click="submitAdd">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="openUpdateModel" title="修改车位信息" width="600">
      <el-form :model="updateForm">
        <el-form-item label="区域" label-width="180" required>
          <el-select v-model="updateForm.areaId" placeholder=" ">
            <el-option v-for="item in area" :key="item.id" :label="item.name" :value="item.id" style="width: 225px;" />
          </el-select>
        </el-form-item>
        <el-form-item label="车位编号" label-width="180" required>
          <el-input v-model="updateForm.code" autocomplete="off" style="width: 225px;" />
        </el-form-item>
        <el-form-item label="车位类型" label-width="180" required>
          <el-select v-model="updateForm.type" placeholder=" " style="width: 225px;">
            <el-option label="机动车" :value="1" />
            <el-option label="二轮车" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="openUpdateModel = false">取消</el-button>
          <el-button type="primary" @click="submitUpdate">
            确定
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