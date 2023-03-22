<script setup>
// 余位信息管理页面
import CarNumKeyboard from "../../../../components/CarNumKeyboard.vue";
import { ref, computed, onMounted, reactive } from "vue";
import { useRouter } from "vue-router";
import { get, post } from "../../../../tool/http";
import { ElMessage } from "element-plus";
const route = useRouter()


// const statusList = ref([{value:0,label:}])

const data = ref()
const cartTotal = ref(0)
const motorcycleTotal = ref(0)
function getInfo () {
  get('/parking/getRemainder').then(res => {
    data.value = res.data
    for (let i = 0; i < data.value.length; i++) {
      cartTotal.value = cartTotal.value + data.value[i].car
      motorcycleTotal.value = motorcycleTotal.value + data.value[i].motorcycle
    }
  })
}
getInfo()
const type = ref(1)
function openParkForm (value) {
  type.value = value
  if (type.value == 1) {
    title.value = '停车'
  } else {
    title.value = '取车'
  }
  datetime.value = new Date().getTime()
  openForm.value = true

}
const openForm = ref(false)
const title = ref('停车')
const child = ref()
const datetime = ref(new Date().getTime())
const form = reactive({
  num: '',
  type: ''
})
function getCarNum (data) {
  form.num = data
}
function parkAction(){
  if (type.value == 1) {
    park()
  } else {
    pickup()
  }
}
function park () {
  if (!form.type) {
    ElMessage.error('请选择车辆类型')
    return
  }
  if (!child.value.checkInputFinshed()) {
    return
  }
  post('/parking/front/parkByCarNum', form).then(res => {
    if (res.code == 401) {
      ElMessage.error(res.msg)
      router.push('/')
    } else if (res.code == 200) {
      ElMessage.success("停车成功")
      getInfo()
      openForm.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })

}


function pickup () {
  if (!child.value.checkInputFinshed()) {
    return
  }
  post('/parking/back/pickUpByCarNum', {
    num:form.num,
    returnUrl: '/back/home'
  }).then(res => {
    if (res.code == 200) {
      document.querySelector('body').innerHTML = res.data
      document.forms[0].submit()
    }
    else if (res.code == 201) {
      ElMessage.success(res.msg)
      getInfo()
      openForm.value = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>
<template>
  <div class="all-c">
    <h1>余位信息管理</h1>
    <el-divider />

    <div class="title">当前车位剩余机动车位{{ cartTotal }}位，二轮车位{{ motorcycleTotal }}位，各区余位如下</div>
    <el-divider />

    <div class="btn">
      <el-button type="primary" @click="openParkForm(1)">停车</el-button>
      <el-button type="primary" @click="openParkForm(2)">取车</el-button>
    </div>
    <div class="contain-c">
      <el-table :data="data" style="width: 540px" :cell-style="{ textAlign: 'center' }" stripe
        :header-cell-style="{ 'text-align': 'center' }">
        <el-table-column prop="area" label="区域" width="180" />
        <el-table-column prop="car" label="机动车" width="180" />
        <el-table-column prop="motorcycle" label="二轮车" width="180" />
      </el-table>
    </div>
  </div>

  <el-dialog v-model="openForm" :title="title" width="700">
    <el-form-item required v-if="type == 1">
      <el-select v-model="form.type" placeholder="请选择车辆类型" style="width: 70%;margin: 0 auto;" clearable>
        <el-option label="机动车" :value="1" />
        <el-option label="二轮车" :value="2" />
      </el-select>
    </el-form-item>
    <el-form-item required>
      <CarNumKeyboard @sendCarNum="getCarNum" :key="datetime" ref="child" style="width: 70%; " />
    </el-form-item>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="openForm = false">取消</el-button>
        <el-button type="primary" @click="parkAction">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.all-c {
  padding-left: 30px;
}

.contain-c {
  padding-top: 30px;
  padding-left: 10px;
  margin: 0 auto;
  text-align: center;
  display: flex;
  justify-content: center;

}

.btn {
  width: 100%;
  text-align: center;
}

.title {
  width: 100%;
  text-align: center;
}
</style>