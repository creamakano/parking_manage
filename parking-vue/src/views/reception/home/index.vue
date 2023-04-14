<script setup>
import { ref } from "@vue/reactivity";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { post } from "../../../tool/http";
const route = useRouter()
const activeIndex = ref('/front/home')

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

function logout () {
  post('/user/logout').then(res => {
    ElMessage.success('登出成功')
    route.push('/')
  })
}
</script>

<template>
  <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" router="true">
    <el-sub-menu index="/front/home/parking">
      <template #title>车辆停车</template>
      <el-menu-item index="/front/home/parking">停车取车</el-menu-item>
      <el-menu-item index="/front/home/publicPlace">所有车位</el-menu-item>
    </el-sub-menu>

    <el-menu-item index="/front/home/charge">车辆充电</el-menu-item>

    <el-sub-menu index="/front/home">
      <template #title>我的信息</template>
      <el-menu-item index="/front/home/baseInfo">基本信息</el-menu-item>
      <el-menu-item index="/front/home/carNum">我的车牌</el-menu-item>
      <el-menu-item index="/front/home/myPlace">我的车位</el-menu-item>
      <el-menu-item index="/front/home/buyPlace">购买车位</el-menu-item>
      <el-menu-item index="/front/home/buyCard">优惠套餐</el-menu-item>
      <el-menu-item index="/front/home/myCard">我的套餐</el-menu-item>
    </el-sub-menu>

    <el-sub-menu index="/front/home/parkingLog">
      <template #title>停车充电记录</template>
      <el-menu-item index="/front/home/parkingLog">停车记录</el-menu-item>
      <el-menu-item index="/front/home/chargeLog">充电记录</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="#" @click="logout">安全退出</el-menu-item>


  </el-menu>

  <router-view></router-view>
</template>