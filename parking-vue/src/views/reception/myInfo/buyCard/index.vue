<script setup>
import { ref } from 'vue';
import { post } from '../../../../tool/http';
import CarNumKeyboard from '../../../../components/CarNumKeyboard.vue';
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router';
const router = useRouter()
function insertCard (days) {

  post('/card/checkCarNum')

  post('/card/insert')
}


//获取编辑dialog的车牌号
const carNum = ref('')
const child = ref()
const datetime = ref(new Date().getTime())
function getCarNum (data) {
  carNum.value = data
}
const type = ref(1)
const openModelFlag = ref(false)
const modelTitle = ref('')
function openModel (value) {
  type.value = value
  datetime.value = new Date().getTime()
  if (type.value == 1) {
    modelTitle.value = '购买年卡'
  } else {
    modelTitle.value = '购买月卡'
  }
  openModelFlag.value = true
}

function cardInsert () {
  if (!child.value.checkInputFinshed()) {
    return
  }
  post('/card/checkCarNum', {
    carNum: carNum.value
  }).then(res => {
    if (res.code == 401) {
      ElMessage.error(res.msg)
      router.push('/')
    } else if (res.code == 200) {
      if (!res.data) {
        open('该车牌不是您的车牌，是否购买')
      } else {
        post('/card/insert', {
          carNum: carNum.value,
          type: type.value,
          returnUrl: '/front/home/buyCard'
        }).then(res => {
          document.querySelector('body').innerHTML = res.data
          document.forms[0].submit()
        })
      }
    }
  })

}

const open = (msg) => {
  ElMessageBox.confirm(
    msg,
    '警告 ',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      post('/card/insert', {
        carNum: carNum.value,
        type: type.value,
        returnUrl: '/front/home/buyCard'
      }).then(res => {
        document.querySelector('body').innerHTML = res.data
        document.forms[0].submit()
      })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消购买',
      })
      openModelFlag.value = false
    })
}
</script>

<template>
  <div class="all-c">
    <h1>优惠套餐</h1>
    <el-divider />
    <div class="contain-c">

      <div class="card-c"
        style="background-image: url('/src/assets/icons/year-card.png');  background-repeat:no-repeat; background-size: 500px 300px;">
        <div class="title">
          <h2>停车场年卡</h2>
        </div>
        <div class="footer">
          <div>￥2888</div>
          <el-button @click="openModel(1)">购买</el-button>
        </div>
      </div>

      <div class="card-c"
        style="background-image: url('/src/assets/icons/month-card.png');  background-repeat:no-repeat; background-size: 500px 300px;">
        <div class="title">
          <h2>停车场月卡</h2>
        </div>
        <div class="footer">
          <div>￥280</div>
          <el-button @click="openModel(2)">购买</el-button>
        </div>
      </div>


    </div>
  </div>



  <el-dialog v-model="openModelFlag" :title="modelTitle" width="600">
    <el-form :model="parkForm">
      <el-form-item>
        <CarNumKeyboard @sendCarNum="getCarNum" :key="datetime" ref="child" style="width:360px" />
      </el-form-item>

    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="openModelFlag = false">取消</el-button>
        <el-button type="primary" @click="cardInsert">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style>
.contain-c {
  width: 100%;
  display: flex;
  justify-content: space-around;
  padding-top: 20px;
}

.card-c {
  width: 500px;
  height: 300px;
  border: 1px solid black;
  border-radius: 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.title {
  text-align: center;
}

.footer {
  width: 100%;
  display: flex;
  justify-content: space-between;
  padding: 15px;
  box-sizing: border-box;
}
</style>