import { createStore } from 'vuex'
/**
 * 创建仓库和导出
 */
const store  = createStore({
    state: {
        num: 111,
        userInfo:{
          userId: "",
          userName: "",
          creationTime:"",
        },
        employee:{
          
        }
    },
    mutations: {
      setUserInfo(state,payload){
        state.userInfo = payload;
      },
      setEmployee(state,payload){
        state.employee = payload;
      },
      setNum(state , payload){
        state.num = payload
      }
      
  }
})

export default store