export default{
    namespaced:true,
    state:()=>{
        return{

        }
    },
    getters:{

    },
    mutations:{
        login(state,payload){
            state.userInfo={...payload}
        },
        logout(state){
            state.userInfo=null
        }
    },
    actions:{
    },
    modules:{
    }
}