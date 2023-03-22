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
        <el-col :span="2" v-for="(item, id) in keyNums" :key="id" style=" margin-top: 10px;">
          <el-button class="typer-num" :class="{ 'is-A': item === 'A', 'is-OK': item === 'OK', 'is-Del': item === 'Del' }"
            @click="input(item)">{{
              item
            }}</el-button>
        </el-col>
      </el-row>
    </div>
    <el-dialog title="拨打电话" :visible.sync="telPhone" width="100%" @close="telPhoneClose">
      <div style="padding-bottom:20px ">当前车牌号：{{ this.carNumber }}</div>
      <el-button type="primary" @click="callPhone">拨 号</el-button>
      <el-button @click="telPhoneClose">取 消</el-button>
    </el-dialog>
  </div>
</template>
<script>
import { ElMessage } from 'element-plus'
export default {
  data () {
    return {
      phoneNo: '',
      telPhone: false,
      showTyper: 0, // 输入法的值，0表示不显示，1表示显示省输入键盘，2表示显示数字字母输入键盘
      provinces: ['京', '沪', '浙', '苏', '粤', '鲁', '晋', '冀', // 省
        '豫', '川', '渝', '辽', '吉', '黑', '皖', '鄂',
        '津', '贵', '云', '桂', '琼', '青', '新', '藏',
        '蒙', '宁', '甘', '陕', '闽', '赣', '湘', '关闭'],
      keyNums: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', // 数字字母
        'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
        'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L',
        'Z', 'X', 'C', 'V', 'B', 'N', 'M', 'OK', 'Del'],
      carNumber: '', // 输入的值
      wechatCode: '',
      wechatStatus: ''

    }
  },
  created () {
    this.conPa()
  },
  methods: {
    conPa () {
      this.wechatCode = this.$route.query.code
      this.wechatStatus = this.$route.query.state
    },
    telPhoneClose () {
      this.telPhone = false
    },
    callPhone () {
      window.location.href = 'tel:' + this.phoneNo
    },
    clearNum () {
      this.carNumber = ''
    },
    async onOfferTap () { // 对最终结果进行判断
      var carNumberReg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/
      if (!carNumberReg.test(this.carNumber)) {
        this.$message('请输入正确的车牌号码')
        return
      }
      if (this.carNumber.length < 6) {
        ElMessage.error('车牌有误，请重新输入')
        return
      }

    },
    onTypewriting: function () { // 点击输入框时，弹出键盘
      this.showTyper = 1
      this.changeTyper()
    },
    changeTyper: function () { // 判断输入的车牌号处于什么状态，为空？已输入第一个值（即省）？输入省之后的值？
      if (this.carNumber.length >= 1) {
        this.showTyper = 2
      }
      if (this.carNumber.length === 0) {
        this.showTyper = 1
      }
    },
    checkCarNum: function (that) {
      var carNumberReg = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/
      if (!carNumberReg.test(that.carNumber)) {
        ElMessage.error('请输入正确的车牌号码')
        return false
      }
      if (this.carNumber.length < 6) {
        ElMessage.error('车牌有误，请重新输入')
        return false
      }
      return true
    },
    input: function (item) { // 键盘点击事件，传入键盘本身的值
      if (item === 'OK' || item === '关闭') { // 判断是否点击了关闭按钮
        var that = this
        if (!this.checkCarNum(that)) {
          return
        }
        this.showTyper = 0

        this.$emit('sendCarNum', this.carNumber);
        return
      }
      if (item === 'Del') { // 判断是否点击了删除按钮
        this.carNumber = this.carNumber.slice(0, -1)
        this.changeTyper()
        return
      }
      if (this.carNumber.length < 7) { // 判断当前的车牌号的数目是否合法，还未超出7位则可继续输入
        this.carNumber = this.carNumber + item
        this.changeTyper()
      } else {
        ElMessage.error('车牌号码超出正常范围')
      }
    }
  }
}
</script>
<style >
#main {
  width: 30%;
  margin: 20px auto;
}
</style>
