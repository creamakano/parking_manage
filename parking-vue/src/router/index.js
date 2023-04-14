import { createRouter, createWebHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'
import BackLoginView from '../views/backstage/login/index.vue'
import BackHomeView from '../views/backstage/home/index.vue'
import FrontHomeView from '../views/reception/home/index.vue'
import PayView from '../views/pay/test.vue'
import { awaitGet } from '../tool/http.js'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: BackLoginView
    },
    {
      path: '/pay',
      component: PayView
    },
    {
      path: '/backLogin',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: BackLoginView
    },
    {
      path: '/back/home',
      component: BackHomeView,
      children: [
        {
          path: "",
          component: () => import('../views/backstage/parkingManage/remainder/index.vue')
        },
        {
          path: "parking",
          component: () => import('../views/backstage/parkingManage/parking/index.vue')
        },
        {
          path: "parkingLog",
          component: () => import('../views/backstage/parkingManage/parkingLog/index.vue')
        },
        {
          path: "rule",
          component: () => import('../views/backstage/parkingManage/rule/index.vue')
        },
        {
          path: "place",
          component: () => import('../views/backstage/placeManage/place/index.vue')
        },
        {
          path: "area",
          component: () => import('../views/backstage/placeManage/area/index.vue')
        },
        {
          path: "charge",
          component: () => import('../views/backstage/chargeManage/charge/index.vue')

        },
        {
          path: "chargeLog",
          component: () => import('../views/backstage/chargeManage/chargeLog/index.vue')

        },
        {
          path: "userInfo",
          component: () => import('../views/backstage/userManage/userInfo/index.vue')

        },
        {
          path: "employeeInfo",
          component: () => import('../views/backstage/userManage/employeeInfo/index.vue')

        },


      ]
    }
    ,
    {
      path: '/front/home',
      component: FrontHomeView,
      children: [
        {
          path: "carNum",
          component: () => import('../views/reception/myInfo/carNum/index.vue')
        },
        {
          path: "parking",
          component: () => import('../views/reception/parking/carNum/index.vue')
        },
        {
          path: "",
          component: () => import('../views/reception/parking/carNum/index.vue')
        },
        {
          path: "charge",
          component: () => import('../views/reception/charge/index.vue')
        },
        {
          path: "buyPlace",
          component: () => import('../views/reception/myInfo/buyPlace/index.vue')
        },
        {
          path: "myPlace",
          component: () => import('../views/reception/myInfo/myPlace/index.vue')
        },
        {
          path: "baseInfo",
          component: () => import('../views/reception/myInfo/baseInfo/index.vue')
        },
        {
          path: "publicPlace",
          component: () => import('../views/reception/parking/publicPlace/index.vue')
        },
        {
          path: "buyCard",
          component: () => import('../views/reception/myInfo/buyCard/index.vue')
        },
        {
          path: "myCard",
          component: () => import('../views/reception/myInfo/myCard/index.vue')
        },
        {
          path: "parkingLog",
          component: () => import('../views/reception/log/parkLog/index.vue')
        },
        {
          path: "chargeLog",
          component: () => import('../views/reception/log/chargeLog/index.vue')
        },
      ]
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  if (to.path == '/back/' ) {
    if (store.state.employee.position == '') {
      await awaitGet('/employee/session').then(res => {
        if (res.code == 200) {
          store.commit('setEmployee', res.data)
        } else {
          ElMessage.error(res.msg)
          router.push('/')
        }
      })
    }
    if (to.path == '/back/employeeInfo' && store.state.employee.position != null && store.state.employee.position < 1) {
      ElMessage.error("权限不足")
      router.push('/')
    }
  }

  next()//需要调用next()才能放行
})


export default router
