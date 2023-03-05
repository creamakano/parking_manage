import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { axios } from '../src/tool/http.js'
import store from '../src/store/index.js'
import 'element-plus/theme-chalk/index.css'
import App from './App.vue'
import router from './router'


const app = createApp(App)
app.config.globalProperties.$axios = axios

app.use(router)
app.use(ElementPlus)
app.use(store)

app.mount('#app')
