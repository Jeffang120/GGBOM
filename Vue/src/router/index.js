import { createRouter, createWebHashHistory } from 'vue-router'
import Home from '../views/Home.vue'
const Login=()=>import('@/views/Login.vue')
const Attribute=()=>import('@/views/select/Attribute.vue')
const Classify=()=>import('@/views/select/Classify.vue')
const PartBom=()=>import('@/views/select/PartBom.vue')
const AddClassify=()=>import('@/views/AddClassify.vue')
const ModifyClassify=()=>import('@/views/ModifyClassify.vue')

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
    redirect: '/login',
    children:[
      {
        path:'/attribute',
        component: Attribute,
      },{
        path:'/classify',
        component: Classify,
      },{
        path:'/partbom',
        component: PartBom,
      }
    ]
  },{
    path:'/login',
    name:'login',
    component: Login
  },{
    path:'/addclassify',
    name:'addclassify',
    component: AddClassify
  },{
    path:'/modifyclassify/:id',
    name:'modifyclassify',
    component: ModifyClassify
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
