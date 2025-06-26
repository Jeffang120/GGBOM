<template>
  <div class="home-container">
    <el-container>
      <el-aside width="250px" class="sidebar">
        <div class="logo-container">
          <img src="@/assets/faviconT.png" alt="GGBOM Logo" class="logo-img" style="size: 5px;">
          <h2 class="logo-text">GGBOM</h2>
        </div>
        <el-menu
          :default-active="activeIndex"
          class="el-menu-vertical"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-menu-item index="1" @click="toAttr()">
            <el-icon><Setting /></el-icon>
            <span>属性管理</span>
          </el-menu-item>
            
          <el-menu-item index="2" @click="toClassify()">
            <el-icon><document /></el-icon>
            <span>分类管理</span>
          </el-menu-item>

          <el-menu-item index="3" @click="toPB()">
            <el-icon><Menu /></el-icon>
            <span>Part&Bom管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="main-header">
          <div class="header-content">
            <div class="breadcrumb">
              <h3>Global Genesis Bill of Materials Management System - 全局创源物料清单管理系统</h3>
            </div>
            <div class="user-info">
              <span class="username">{{ account }}</span>
              <el-button type="primary" class="logout-btn" @click="logout()">登出</el-button>
            </div>
          </div>
        </el-header>

        <el-main class="main-content">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { useRouter ,useRoute} from 'vue-router';
import {useStore} from 'vuex';
import {computed,watch} from 'vue';
export default {
  setup(){
    const router = useRouter();
    const route = useRoute();
    const store=useStore();
    if(!store.state.user.userInfo){
      router.push('/login')
      return
    }
   
    let account=computed(()=>{
      if(store.state.user.userInfo){
        return store.state.user.userInfo.account;
      }
    });
    
    console.log(account)
    const toAttr=()=>{
      router.push('/attribute')
    }
    const toClassify=()=>{
      router.push('/classify')
    }
    const toPB=()=>{
      router.push('/partbom')
    }
    const logout=()=>{
      router.push('/login')
      store.commit('user/logout')
    }
    const activeIndex = computed(() => {
      const path = route.path;
      switch (path) {
        case '/attribute':
          return '1';
        case '/classify':
          return '2';
        case '/partbom':
          return '3';
        default:
          return '1';
      }
    });
    return{
      toAttr,
      toClassify,
      toPB,
      account,
      logout,
      activeIndex
    }
  }
}
</script>

<style lang="less" scoped>
.home-container {
  min-height: 100vh;
  background-color: #f5f7fa;

  .sidebar {
    background: linear-gradient(145deg, #4481eb, #04befe);
    color: white;
    height: 100vh;
    box-shadow: 2px 0 10px rgba(0,0,0,0.1);

    .logo-container {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-bottom: 1px solid rgba(255,255,255,0.1);
      .logo-img{
        width: 30px;
        height: 30px;
        margin-right: 5px;
      }
      .logo-text {
        color: white;
        font-size: 24px;
        margin: 0;
      }
    }

    .el-menu-vertical {
      border-right: none;
      background: transparent;

      .el-menu-item {
        color: white;
        
        &:hover, &.is-active {
          background: rgba(255,255,255,0.1);
          color: white;
        }

        i {
          color: white;
        }
      }
    }
  }

  .main-header {
    background: white;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
    padding: 0;

    .header-content {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 20px;

      .breadcrumb {
        h3 {
          color: #333;
          margin: 0;
        }
      }

      .user-info {
        display: flex;
        align-items: center;
        gap: 15px;

        .username {
          color: #666;
          font-size: 14px;
        }

        .logout-btn {
          background: linear-gradient(145deg, #4481eb, #04befe);
          border: none;
          padding: 8px 20px;
          border-radius: 20px;

          &:hover {
            opacity: 0.9;
          }
        }
      }
    }
  }

  .main-content {
    padding: 20px;
    background: #f5f7fa;
  }
}
</style>