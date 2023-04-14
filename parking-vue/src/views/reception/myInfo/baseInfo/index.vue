<script setup>
import { reactive, ref } from "vue";
import { get, put } from "../../../../tool/http";
import {
  Iphone,
  Location,
  OfficeBuilding,
  Tickets,
  User,
  Male,
  Present
} from '@element-plus/icons-vue'
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

const route = useRouter()
const user = ref()

const levelInfo = ref(
  {
    4: {
      src: '/src/assets/icons/zuanshihuiyuan.svg',
      name: '钻石会员',
      target: '10000+'
    },
    3: {
      src: '/src/assets/icons/golden.svg',
      name: '黄金会员',
      target: '10000'
    },
    2: {
      src: '/src/assets/icons/baiyinhuiyuan.svg',
      name: '白银会员',
      target: '1000'
    },
    1: {
      src: '/src/assets/icons/qingtonghuiyuan.svg',
      name: '青铜会员',
      target: '500'
    },
  }
)
const level = ref(1)
const point = ref(1)
const percentageOne = ref(0)
const percentageTwo = ref(0)
const percentageThree = ref(0)
const msg = ref('')


//个人信息
const userInfo = reactive({
  id: '',
  name: '',
  phone: '',
  sex: '',
  address: '',
  birth: ''
})
const sex = reactive({
  0: '男',
  1: '女'
})




function getData () {
  level.value = user.value.level
  point.value = user.value.point
  console.log(level.value);
  if (level.value == 4) {
    msg.value = '您已达到最高等级'
    percentageOne.value = 100
    percentageTwo.value = 100
    percentageThree.value = 100
  } else if (level.value == 3) {
    const val = 10000 - point.value
    msg.value = '您还差' + val + '积分到达下一等级'
    percentageOne.value = 100
    percentageTwo.value = 100
    percentageThree.value = (point.value - 5000) / 50
    console.log(percentageThree.value);
  } else if (level.value == 2) {
    const val = 5000 - point.value
    msg.value = '您还差' + val + '积分到达下一等级'
    percentageOne.value = 100
    percentageTwo.value = (point.value - 500) / 5
    percentageThree.value = 0
  } else {
    const val = 500 - point.value
    msg.value = '您还差' + val + '积分到达下一等级'
    percentageOne.value = point.value / 5
    percentageTwo.value = 0
    percentageThree.value = 0
    console.log("other");
  }

  userInfo.id = user.value.id;
  userInfo.name = user.value.name;
  userInfo.phone = user.value.phone;
  userInfo.sex = user.value.sex;
  userInfo.address = user.value.address;
  userInfo.birth = user.value.birth;

}
function getUser () {

  get('/user/getInfo').then(res => {
    if (res.code == 200) {
      user.value = res.data
      getData()
    } else if (res.code == 401) {
      ElMessage.error("登录信息过期，请重新登录")
      route.push('/')
    }
  })
}

getUser()
function updateUserInfo () {
  put('/user/update', userInfo).then(res => {
    if (res.code == 200) {
      ElMessage.success('修改成功')
      getUser()
    }
  })
}

</script>
<template>
  <div style="padding-top: 100px;">

    <div class="level">
      <div class="level-item">
        <div style="width: 200px;height: 110px;">
          <img :src="levelInfo[level].src" class="image" />
        </div>
        <div class="level-top-right">
          <div
            style=" background-color: rgb(250,225,159); border-bottom: 1px solid  white;border-left: 1px solid  white; padding: 5px 20px; border-bottom-left-radius: 10px; width: 70px;">
            当前等级</div>
          <div style="padding: 20px; font-size: 34px; font-weight: bolder;">
            {{ levelInfo[level].name }}
          </div>
        </div>
      </div>
      <div class="level-item">
        <div style="padding-left: 20px;"><span style="font-size: 36px;">{{ point }}/</span><span
            style="font-size: 28px;">{{ levelInfo[level].target }}</span></div>
        <div style="height: 42px; line-height: 42px;padding-right: 20px;">{{ msg }}</div>
      </div>
      <div class="level-bottom">
        <div style="display: flex; padding: 0 20px;">
          <el-progress :percentage="percentageOne" :show-text="false" style="width:50px" status="warning" />
          <el-progress :percentage="percentageTwo" :show-text="false" style="width:140px" status="warning" />
          <el-progress :percentage="percentageThree" :show-text="false" style="width:270px" status="warning" />
        </div>
        <div class="level-bottom-text" style="display: flex; padding-left:7px;padding-top: 10px;">
          <div style="width:50px">青铜</div>
          <div style="width:140px">白银</div>
          <div style="width:270px">黄金</div>
          <div>钻石</div>
        </div>
      </div>
    </div>

    <div class="info">
      <el-descriptions class="margin-top" title="个人信息" :column="2" :size="size" border>
        <template #extra>
          <el-button type="primary" @click="updateUserInfo">确认修改</el-button>
        </template>
        <el-descriptions-item width="200">
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <User />
              </el-icon>
              用户名
            </div>
          </template>
          <el-input type="text" style="width: 300px;" v-model="userInfo.name" />
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <iphone />
              </el-icon>
              手机号码
            </div>
          </template>
          <el-input type="text" style="width: 350px;" v-model="userInfo.phone" />
        </el-descriptions-item>
        <el-descriptions-item width="200">
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Male />
              </el-icon>
              性别
            </div>
          </template>
          <el-select v-model="userInfo.sex" class="m-2" placeholder="Select">
            <el-option label="男" :value="0" />
            <el-option label="女" :value="1" />

          </el-select>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <present />
              </el-icon>
              生日
            </div>
          </template>
          <el-date-picker v-model="userInfo.birth" type="date" placeholder=" " value-format="YYYY-MM-DD"
            format="YYYY-MM-DD" />
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <location />
              </el-icon>
              地址
            </div>
          </template>
          <el-input type="text" v-model="userInfo.address" />
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>
<style scoped>
.info {
  width: 1000px;
  margin: 40px auto;
}

.level {
  border: 1px solid lightgrey;
  border-radius: 10px;
  position: relative;
  z-index: 1;
  padding-bottom: 30px;
  width: 500px;
  margin: 0 auto;
  background-color: rgb(245, 223, 174);
}

.level-item {
  display: flex;
  justify-content: space-between;
  position: relative;
  z-index: 1;

}

.level-top-right {
  display: flex;
  flex-direction: column;
  align-items: end;
}

.image {
  position: absolute;
  top: -90px;
  z-index: 999;
}

.level-bottom {

  padding-top: 20px;
}

.level-bottom-text div {
  font-size: 14px;
}
</style>