<template>
  <div class="all-c">
    <div style="margin: 20px auto;width: 30%;">
      <el-select v-model="type" placeholder="请选择车辆类型" style="width: 100%;" clearable>
        <el-option label="机动车" :value="1" />
        <el-option label="二轮车" :value="2" />
      </el-select>
    </div>
    <CarNumKeyboard @sendCarNum="getCarNum" :key="datetime" ref="child" style="width: 30%;margin-bottom: 20px;" />
    <div class="bottom-btn">
      <el-button type="success" @click="park">停车</el-button>
      <el-button type="danger" @click="pickup">取车</el-button>
    </div>
  </div>
</template>
<script setup> 
import { ElMessage } from 'element-plus';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import CarNumKeyboard from '../../../../components/CarNumKeyboard.vue'
import { post } from '../../../../tool/http';
const router = useRouter()

const child = ref()
const datetime = ref(new Date().getTime())
const carNum = ref('')
const type = ref('')
function getCarNum (data) {
  carNum.value = data
}

function park () {
  if (!type.value) {
    ElMessage.error('请选择车辆类型')
    return
  }
  if (!child.value.checkInputFinshed()) {
    return
  }
  post('/parking/front/parkByCarNum', {
    num: carNum.value,
    type: type.value
  }).then(res => {
    if (res.code == 401) {
      ElMessage.error(res.msg)
      router.push('/')
    } else if (res.code == 200) {
      ElMessage.success("停车成功")
      datetime.value = new Date().getTime()
    } else {
      ElMessage.error(res.msg)
    }
  })

}

function pickup () {
  if (!child.value.checkInputFinshed()) {
    return
  }
  post('/parking/front/pickUpByCarNum', {
    num: carNum.value,
    returnUrl: '/front/home/parking'
  }).then(res => {
    if (res.code == 200) {
      datetime.value = new Date().getTime()
      document.querySelector('body').innerHTML = res.data
      document.forms[0].submit()
    }
    else if (res.code == 201) {

      datetime.value = new Date().getTime()
      ElMessage.success(res.msg)
      getPage()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>

<style scoped>
.bottom-btn {
  margin: 0 auto;
  width: fit-content;
}

.all-c {
  padding-top: 30px;
}
</style>