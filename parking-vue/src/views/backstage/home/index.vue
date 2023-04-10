<script setup>
import { ref } from "@vue/reactivity";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { post } from "../../../tool/http";
const route = useRouter()
const activeIndex = ref('/back/home')

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}
function logout () {
  post('/employee/logout').then(res => {
    ElMessage.success("登出成功")
    route.push('/')
  })
}
</script>

<template>
  <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" router="true">
    <el-sub-menu index="/back/home">
      <template #title>停车管理</template>
      <el-menu-item index="/back/home">余位管理</el-menu-item>
      <el-menu-item index="/back/home/parking">停车信息管理</el-menu-item>
      <!-- <el-menu-item index="/back/home/rule">收费规则</el-menu-item> -->
      <el-menu-item index="/back/home/parkingLog">停车记录</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="/back/home/place">
      <template #title>车位管理</template>
      <el-menu-item index="/back/home/place">车位管理</el-menu-item>
      <el-menu-item index="/back/home/area">区域管理</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="/back/home/charge">
      <template #title>充电管理</template>
      <el-menu-item index="/back/home/charge">充电车位管理</el-menu-item>
      <el-menu-item index="/back/home/chargeLog">充电记录管理</el-menu-item>
    </el-sub-menu>
    <el-sub-menu index="/back/home/userInfo">
      <template #title>用户管理</template>
      <el-menu-item index="/back/home/userInfo">用户信息管理</el-menu-item>
      <el-menu-item index="/back/home/employeeInfo" v-if="$store.state.employee.position == 1">员工信息管理</el-menu-item>
    </el-sub-menu>
    <el-menu-item index="#" @click="logout">安全退出</el-menu-item>

  </el-menu>

  <router-view></router-view>
</template>