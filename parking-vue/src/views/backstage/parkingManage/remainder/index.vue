<script setup>
// 余位信息管理页面

import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { get } from "../../../../tool/http";
const route = useRouter()


// const statusList = ref([{value:0,label:}])

const data = ref()
const cartTotal = ref(0)
const motorcycleTotal = ref(0)
get('/parking/getRemainder').then(res => {
  data.value = res.data
  for (let i = 0; i < data.value.length; i++) {
    cartTotal.value = cartTotal.value + data.value[i].car
    motorcycleTotal.value = motorcycleTotal.value + data.value[i].motorcycle
  }
})

function toParking () {
  route.push('/back/home/parking')
}


</script>
<template>
  <div class="all-c">
    <h1>余位信息管理</h1>
    <el-divider />

    <div class="title">当前车位剩余机动车位{{ cartTotal }}位，二轮车位{{ motorcycleTotal }}位，各区余位如下</div>
    <el-divider />

    <div class="btn">
      <el-button type="primary" @click="toParking">停车</el-button>
      <el-button type="primary" @click="toParking">取车</el-button>
      <!-- <el-select v-model="value" class="m-2" placeholder="Select" size="large">
          <el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select> -->
      <!-- <el-input v-model="input" placeholder="Please input" clearable />
        <el-form :model="form" label-width="120px">
          <el-form-item label="">
            <el-input v-model="form.name" />
          </el-form-item>
        </el-form> -->
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