<script setup>
import { ref, reactive } from "@vue/reactivity"
import { post, get } from '../../../tool/http.js'
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import { first } from "lodash";
const route = useRouter()
const store = useStore()
const form = reactive({
  name: '',
  password: '123',
  phone: '18320982069'
})
const ruleForm = reactive({
  password: '',
  checkPass: '',
  phone: '',
})

function login () {
  post('/user/login', form).then((res) => {
    if (res.code == 200) {
      store.commit('setUserInfo', res.data)
      route.push('/front/home/')
    }
  })
}
const activeName = ref('first')
function registry () {
  post('/user/registry', registryForm).then(res => {
    if (res.code == 200) {
      ElMessage.success("注册成功")

    } else {
      ElMessage.error(res.msg)
    }

  })
  activeName.value = "first"
}

const registryForm = reactive({
  phone: '',
  password: '',
  confirmPassword: ''
})


const backForm = reactive({
  code: '',
  password: ''
})

function backLogin () {
  post('/employee/login', backForm).then(res => {
    if (res.code == 200) {
      store.commit('setEmployee', res.data)
      ElMessage.success('登录成功')
      route.push('/back/home')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>
<template>
  <div class="main-c">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="登录" name="first">
        <el-form :model="form" label-width="120px">

          <el-form-item label="手机号码">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" />
          </el-form-item>

        </el-form>
        <div class="center">
          <el-button type="primary" plain @click="login">登录</el-button>

        </div>

      </el-tab-pane>

      <el-tab-pane label="注册" name="second">
        <el-form :model="registryForm" label-width="120px" class="demo-ruleForm">

          <el-form-item label="手机号码">
            <el-input v-model="registryForm.phone" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="registryForm.password" type="password" />
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="registryForm.confirmPassword" type="password" />

          </el-form-item>
        </el-form>
        <div class="center">
          <el-button type="primary" plain @click="registry">注册</el-button>

        </div>
      </el-tab-pane>


      <el-tab-pane label="后台登录" name="third">
        <el-form :model="backForm" label-width="120px">

          <el-form-item label="员工编号">
            <el-input v-model="backForm.code" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="backForm.password" type="password  " />
          </el-form-item>

        </el-form>
        <div class="center">
          <el-button type="primary" plain @click="backLogin">登录</el-button>

        </div>

      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<style scoped>
.main-c {
  width: 30%;
  margin: 0 auto;

  margin-top: 10%;
}

.center {
  width: 100%;
  margin: 0 auto;
  align-content: center;
  text-align: center;
}
</style>
