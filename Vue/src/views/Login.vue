<template>
  <div class="container">
    <div class="forms-container">
      <div class="signin-signup">
        <!-- 登录表单 -->
        <form action="" class="sign-in-form" @submit.prevent="login">
          <div class="logo-title">
            <img src="@/assets/favicon.png" alt="GGBOM Logo" class="logo-img">
            <b class="system-title">GGBOM</b>
          </div>
          <h2 class="form-title">登录</h2>
          <div class="input-field">
            <div class="el-icon-user-solid"></div>
            <input type="text" placeholder="请输入账号" v-model="account" required>
          </div>
          <div class="input-field">
            <div class="el-icon-key"></div>
            <input :type="showLoginPassword ? 'text' : 'password'" placeholder="请输入密码" v-model="password" required>
            <i :class="showLoginPassword ? 'el-icon-view' : 'el-icon-hide'" @click="showLoginPassword = !showLoginPassword"></i>
          </div>
          <div>
            <el-button type="primary" class="login-btn" native-type="submit">登录</el-button>
          </div>
        </form>

        <!-- 注册表单 -->
        <form action="" class="sign-up-form" @submit.prevent="reg">
          <div class="logo-title">
            <img src="@/assets/favicon.png" alt="GGBOM Logo" class="logo-img">
            <b class="system-title">GGBOM</b>
          </div>
          <h2 class="form-title">注册</h2>
          <div class="input-field">
            <i class="el-icon-user-solid"></i>
            <input type="text" placeholder="请输入账号（6位以上）" v-model="regForm.account" required>
          </div>
          <div class="input-field">
            <i class="el-icon-key"></i>
            <input :type="showRegPassword ? 'text' : 'password'" placeholder="请输入密码（包含数字及大小写字母）" v-model="regForm.password" required>
            <i :class="showRegPassword ? 'el-icon-view' : 'el-icon-hide'" @click="showRegPassword = !showRegPassword"></i>
          </div>
          <div class="input-field">
            <i class="el-icon-key"></i>
            <input :type="showRegConfirmPassword ? 'text' : 'password'" placeholder="请确认密码" v-model="regForm.ensurepassword" required>
            <i :class="showRegConfirmPassword ? 'el-icon-view' : 'el-icon-hide'" @click="showRegConfirmPassword = !showRegConfirmPassword"></i>
          </div>
          <el-button type="primary" class="login-btn" native-type="submit">注册</el-button>
        </form>
      </div>
    </div>

    <div class="panels-container">
      <div class="panel left-panel">
        <div class="content">
          <h3>还没有账号?</h3>
          <p>如果还没有一个账号<br>请您先进行注册</p>
          <button class="btn transparent" id="sign-up-btn" type="button">注册</button>
        </div>
      </div>

      <div class="panel right-panel">
        <div class="content">
          <h3>已有账户?</h3>
          <p>如果已经有账号，<br>请您使用您的账号和密码进行登录。</p>
          <button class="btn transparent" id="sign-in-btn" type="button">登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {ref, reactive} from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex';

export default{
    setup(){
        const router = useRouter();
        const store = useStore()
        const account=ref('')
        const password=ref('')
        const showLoginPassword = ref(false)
        const showRegPassword = ref(false)
        const showRegConfirmPassword = ref(false)
        const regForm = reactive({
            account: '',
            password: '',
            ensurepassword: ''
        })

        const login=()=>{
            //后端验证
            const postData={
                account:account.value,
                password:password.value
            }
            axios.post('/api/User/login',postData)
            .then((res)=>{
                if(res.data.code=='200'){
                    console.log(res.data)
                    store.commit('user/login',{account:account.value})
                    router.push('/attribute')
                }
                else{
                    ElMessage({
                        message: res.data.message,
                        type: 'error',
                        showClose: true,
                    })
                }
                console.log(res.data.message)
            })
            .catch((err)=>{
                console.log(err)
            })
        }

        const reg = () => {
            if (regForm.account.length < 6) {
                ElMessage({
                    message: '账号长度需大于6位',
                    type: 'error',
                    showClose: true,
                })
                return
            }
            if (regForm.password.length < 8) {
                ElMessage({
                    message: '密码长度需大于8位',
                    type: 'error',
                    showClose: true,
                })
                return
            }
            if (!(/\d/.test(regForm.password) && /[A-Z]/.test(regForm.password) && /[a-z]/.test(regForm.password))) {
                ElMessage({
                    message: '密码必须包含数字以及大小写字母',
                    type: 'error',
                    showClose: true,
                })
                return
            }
            if (regForm.password == regForm.ensurepassword) {
                const postData = {
                    account: regForm.account,
                    password: regForm.password
                }
                axios.post('/api/User/register', postData)
                    .then((res) => {
                        if (res.data.code == '200') {
                            console.log(res.data)
                            ElMessage({
                                message: '注册成功，请登录',
                                type: 'success',
                                showClose: true,
                            })
                            // 注册成功后切换到登录面板
                            document.querySelector('.container').classList.remove('sign-up-mode')
                        } else {
                            ElMessage({
                                message: res.data.message,
                                type: 'error',
                                showClose: true,
                            })
                        }
                    })
                    .catch((err) => {
                        console.log(err)
                    })
            } else {
                ElMessage({
                    message: '请再次确认密码',
                    type: 'error',
                    showClose: true,
                })
            }
        }

        return{
            account,
            password,
            login,
            reg,
            regForm,
            showLoginPassword,
            showRegPassword,
            showRegConfirmPassword
        }
    },
    mounted() {
        const sign_in_btn = document.querySelector("#sign-in-btn")
        const sign_up_btn = document.querySelector("#sign-up-btn")
        const container = document.querySelector(".container")

        sign_up_btn.addEventListener('click', () => {
            container.classList.add("sign-up-mode")
        })
        sign_in_btn.addEventListener('click', () => {
            container.classList.remove("sign-up-mode")
        })
    },
    beforeRouteLeave(to, from, next) {
        const container = document.querySelector(".container")
        container.classList.remove("sign-up-mode")
        next()
    }
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

body,
input {
  font-family: 'Poppins', sans-serif;
}

.container {
  position: relative;
  width: 100%;
  min-height: 100vh;
  background-color: #fff;
  overflow: hidden;
}

.forms-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

form {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  grid-column: 1 / 2;
  grid-row: 1 / 2;
  padding: 0 5rem;
  overflow: hidden;
  transition: 0.2s 0.7s ease-in-out;
}

form.sign-in-form {
  z-index: 2;
}

.container::before {
  content: ' ';
  position: absolute;
  width: 2000px;
  height: 2800px;
  border-radius: 50%;
  background: linear-gradient(145deg, #4481eb, #04befe);
  top: -10%;
  right: 48%;
  transform: translateY(-50%);
  z-index: 6;
  transition: 1.8s ease-in-out;
}

form.sign-up-form {
  z-index: 1;
  opacity: 0;
}

.logo-title {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.logo-img {
  width: 40px;
  height: 40px;
}

.system-title {
  font-size: 2.8rem;
  color: #444;
}

.form-title {
  font-size: 1.8rem;
  color: #444;
  margin-bottom: 10px;
}

.input-field {
  max-width: 380px;
  width: 100%;
  height: 55px;
  background-color: #f0f0f0;
  margin: 10px 0;
  border-radius: 55px;
  display: grid;
  grid-template-columns: 15% 70% 15%;
  margin-top: 20px;
  position: relative;
}

.input-field i,
.input-field div {
  text-align: center;
  line-height: 55px;
  color: #acacac;
  font-size: 1.1rem;
  cursor: pointer;
}

.input-field i:last-child:hover {
  color: #4481eb;
}

.input-field input {
  background: none;
  outline: none;
  border: none;
  line-height: 1;
  font-weight: 400;
  font-size: 1.1rem;
  color: #333;
}

.input-field input::placeholder {
  color: #aaa;
  font-weight: 380;
}

.btn {
  width: 150px;
  height: 50px;
  border: none;
  outline: none;
  border-radius: 49px;
  cursor: pointer;
  background-color: #5995fd;
  color: #fff;
  text-transform: uppercase;
  font-weight: 600;
  margin: 10px 0;
  transition: .5s;
}

.btn:hover {
  background-color: #4d84e2;
}

.signin-signup {
  position: absolute;
  top: 45%;
  left: 75%;
  transform: translate(-50%, -50%);
  width: 70%;
  display: grid;
  grid-template-columns: 1fr;
  z-index: 5;
  transition: 1s 0.7s ease-in-out;
}

.panels-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
}

.panel {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-around;
  text-align: center;
  z-index: 7;
}

.left-panel {
  pointer-events: all;
  padding: 3rem 17% 2rem 12%;
}

.panel .content {
  color: #fff;
  transition: .9s .6s ease-in-out;
}

.panel h3 {
  font-weight: 600;
  line-height: 1;
  font-size: 1.5rem;
}

.panel p {
  font-size: 0.95rem;
  padding: 0.7rem 0;
}

.btn.transparent {
  margin: 0;
  background: none;
  border: 2px solid #fff;
  width: 130px;
  height: 41px;
  font-weight: 600;
  font-size: 0.8rem;
}

.right-panel {
  padding: 3rem 12% 2rem 17%;
  pointer-events: none;
}

.right-panel .content {
  transform: translateX(1000px);
}

.container.sign-up-mode::before {
  transform: translate(100%, -50%);
  right: 52%;
}

.login-btn {
  margin-top: 30px;
  margin-bottom: 50px;
  width: 380px;
  height: 50px;
  border-radius: 49px;
}

.container.sign-up-mode .left-panel .content {
  transform: translateX(-1000px);
}

.container.sign-up-mode .right-panel .content {
  transform: translateX(0px);
}

.container.sign-up-mode .left-panel {
  pointer-events: none;
}

.container.sign-up-mode .right-panel {
  pointer-events: all;
}

.container.sign-up-mode .signin-signup {
  left: 25%;
}

.container.sign-up-mode form.sign-in-form {
  z-index: 1;
  opacity: 0;
}

.container.sign-up-mode form.sign-up-form {
  z-index: 2;
  opacity: 1;
}
</style>