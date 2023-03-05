<template>
  <div class="main">
    商户订单<input v-model="out_trade_no"><br>
    订单名称<input v-model="subject"><br>
    付款金额<input v-model="total_amount"><br>
    商品描述<input v-model="body"><br>
    <el-button v-on:click="pay" type="success" round>支付宝支付</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { post } from '../../tool/http';
const out_trade_no = ref('')
const subject = ref('')
const total_amount = ref('')
const body = ref('')

function pay () {
  post('/pay/alipay', {
    out_trade_no: out_trade_no.value,
    subject: subject.value,
    total_amount: total_amount.value,
    body: body.value,
  }).then(res => {
    document.querySelector('body').innerHTML = res
    document.forms[0].submit()
  })
}

</script>