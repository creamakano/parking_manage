<template>
  <div id="main">
    <div class="carNumber" @click="onTypewriting">
      <el-input placeholder="请输入车牌号" v-model="carNumber">
        <el-button type="primary" slot="append" icon="el-icon-search" @click="onOfferTap">查询</el-button>
        <el-button type="primary" slot="append" icon="el-icon-refresh" @click="clearNum">重置</el-button>
      </el-input>
    </div>
    <div class="typer" v-show="showTyper != 0"> <!-- ③输入键盘 -->
      <el-row v-show="showTyper == 1" style="text-align: center; ">
        <!-- ④省的输入 -->
        <el-col :span="3" v-for="(item, id) in provinces" :key="id" style=" margin-top: 10px;">
          <el-button class="typer-pro" :class="{ 'is-closeType': item === '关闭' }" @click="input(item)">
            {{ item }}
          </el-button>
        </el-col>
      </el-row>
      <el-row v-show="showTyper == 2" style="text-align: center; ">
        <!-- ⑤字母数字的输入 -->
        <el-col :span="3" v-for="(item, id) in keyNums" :key="id" style=" margin-top: 10px;">
          <el-button class="typer-num" :class="{ 'is-A': item === 'A', 'is-OK': item === 'OK', 'is-Del': item === 'Del' }"
            @click="input(item)">{{
              item
            }}</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { ref } from 'vue';
import { useRoute } from 'vue-router';

const emit = defineEmits(["sendCarNum"])
const { query } = useRoute()
const showTyper = ref(0)
const provinces = ref(['京', '沪', '浙', '苏', '粤', '鲁', '晋', '冀',
  '豫', '川', '渝', '辽', '吉', '黑', '皖', '鄂',
  '津', '贵', '云', '桂', '琼', '青', '新', '藏',
  '蒙', '宁', '甘', '陕', '闽', '赣', '湘', '关闭'])
const keyNums = ref(['0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
  'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
  'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L',
  'Z', 'X', 'C', 'V', 'B', 'N', 'M', 'OK', 'Del'])
const carNumber = ref('')
const wechatCode = ref('')
const wechatStatus = ref('')
function conPa () {
  wechatCode.value = query.code
  wechatStatus.value = query.state
}

conPa()




function clearNum () {
  carNumber.value = ''
}
async function onOfferTap () { // 对最终结果进行判断
  var carNumberReg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/
  if (!carNumberReg.test(carNumber.value)) {
    ElMessage.error('请输入正确的车牌号码')
    return
  }
  if (carNumber.value.length < 6) {
    ElMessage.error('车牌有误，请重新输入')
    return
  }

}
function onTypewriting () { // 点击输入框时，弹出键盘
  showTyper.value = 1
  changeTyper()
}
function changeTyper () { // 判断输入的车牌号处于什么状态，为空？已输入第一个值（即省）？输入省之后的值？
  if (carNumber.value.length >= 1) {
    showTyper.value = 2
  }
  if (carNumber.value.length === 0) {
    showTyper.value = 1
  }
}
function checkCarNum () {
  var carNumberReg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/
  if (!carNumberReg.test(carNumber.value)) {
    ElMessage.error('请输入正确的车牌号码')
    return false
  }
  if (carNumber.value.length < 6) {
    ElMessage.error('车牌有误，请重新输入')
    return false
  }
  return true
}
function checkInputFinshed () {
  if (!checkCarNum()) {
    return false
  } else if (showTyper.value != 0) {
    ElMessage.error("输入完成后请点击'OK'")
    return false
  }
  return true
}
function input (item) { // 键盘点击事件，传入键盘本身的值
  if (item === 'OK') { // 判断是否点击了关闭按钮
    if (!checkCarNum()) {
      return
    }
    showTyper.value = 0
    emit('sendCarNum', carNumber.value);
    return
  }
  if (item === '关闭') {
    showTyper.value = 0
    return
  }
  if (item === 'Del') { // 判断是否点击了删除按钮
    carNumber.value = carNumber.value.slice(0, -1)
    changeTyper()
    return
  }
  if (carNumber.value.length < 7) { // 判断当前的车牌号的数目是否合法，还未超出7位则可继续输入
    carNumber.value = carNumber.value + item
    changeTyper()
  } else {
    ElMessage.error('车牌号码超出正常范围')
  }
}

defineExpose({
  checkInputFinshed
})
</script>
<style >
#main {
  margin: 0 auto;
}
</style>
